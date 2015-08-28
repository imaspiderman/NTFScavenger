package forensics;

public class NTFScavenger {

	/**************************************
	 * This program is designed to try and pull files off a damaged NTFS file system
	 * by using the MFT records that can be scavenged from a hard drive image.
	 * At no point does this program attempt to write to the file it is pulling data from.
	 * 
	 * @param args
	 */
	private static String _fileName = "";
	private static long _offset = 0;
	private static java.io.FileInputStream _fis;
	private static byte[] _buffer = new byte[1024*1024*4];//4 Meg buffer
	private static byte[] _name = new byte[1024];
	
	public static void main(String[] args) {
		if(args.length < 2) {
			usage();
			return;
		}
		
		try{		
			for(int i=0; i<args.length; i++){
				if(args[i].toLowerCase().equals("-input")){
					_fileName = args[i+1];
				}
				if(args[i].toLowerCase().equals("-offset")){
					_offset = Long.parseLong(args[i+1]);
				}
			}
			
			//Open the input file
			_fis = new java.io.FileInputStream(_fileName);
			_fis.skip(_offset);
			
			//Read Boot Sector
			NTFSDefinitions.init();
			NTFSDefinitions.BootSectorLayout.ReadData(_fis);
			NTFSDefinitions.BootSectorLayout.ShowLayout();
			
			//Scan For Files
			int iBytesRead = 0;
			while((iBytesRead = _fis.read(_buffer)) > 0){
				int offset = searchBuffer(iBytesRead);
				//Set the position to the start of the file record
				_fis.getChannel().position(_fis.getChannel().position() - (iBytesRead - offset));
				NTFSDefinitions.FileRecordLayout.ReadData(_fis);
				System.out.println("\nFile:" + NTFSDefinitions.FileRecordLayout.fileStartPosition);
				NTFSDefinitions.FileRecordLayout.ShowLayout();
								
				if(NTFSDefinitions.FileRecordLayout.GetFieldValueNumericUnsigned("OffsetToFirstAttribute") != 0){
					//Set position to first attribute
					_fis.getChannel().position(NTFSDefinitions.FileRecordLayout.fileStartPosition + 
							NTFSDefinitions.FileRecordLayout.GetFieldValueNumericUnsigned("OffsetToFirstAttribute"));									
					NTFSDefinitions.StandardAttributeResidentLayout.ReadData(_fis);					
					System.out.println("\nAttribute:" + NTFSDefinitions.StandardAttributeResidentLayout.fileStartPosition);
					NTFSDefinitions.StandardAttributeResidentLayout.ShowLayout();
					
					//Loop through the rest of the attributes until the end marker is found or corrupted length is found
					int iCount = 0;
					while(!NTFSDefinitions.StandardAttributeResidentLayout.showRaw("AttributeType").equals("0xFFFFFFFF") &&
							NTFSDefinitions.StandardAttributeResidentLayout.GetFieldValueNumericUnsigned("Length") < 1024){
						_fis.getChannel().position(NTFSDefinitions.StandardAttributeResidentLayout.GetFieldValueNumericUnsigned("Length") + NTFSDefinitions.StandardAttributeResidentLayout.fileStartPosition);					
						NTFSDefinitions.StandardAttributeResidentLayout.ReadData(_fis);
						System.out.println("\nAttribute:" + NTFSDefinitions.StandardAttributeResidentLayout.fileStartPosition);
						NTFSDefinitions.StandardAttributeResidentLayout.ShowLayout();
						//Show Name
						if(NTFSDefinitions.StandardAttributeResidentLayout.showRaw("AttributeType").equals("0x30000000")){
							int iOffsetToAttribute = (int)NTFSDefinitions.StandardAttributeResidentLayout.GetFieldValueNumericUnsigned("OffsetToTheAttribute");
							_fis.getChannel().position(NTFSDefinitions.StandardAttributeResidentLayout.fileStartPosition +
									iOffsetToAttribute +
									0x30);
							_fis.read(_name, 0, 8);//Read file length
							Long fileSize = 0L;
							for(int i=7; i>=0; i--){
								fileSize = (fileSize << 8) | ((int)_name[i] & 0xFF);
							}
							_fis.read(_name, 0, 8);//Read flags
							_fis.read(_name, 0, 2);//Get the length of name in characters and read the filename namespace byte
							int iLength = (int)_name[0] & 0xFF;
							_fis.read(_name, 0, (iLength << 1));//Characters are 2 bytes long
							String sName = "";
							for(int i=0; i<(iLength<<1); i+=2){
								sName += (char)_name[i];
							}
							System.out.println("File Name: " + sName + " Size: " + fileSize.toString());
						}
						if(NTFSDefinitions.StandardAttributeResidentLayout.showRaw("AttributeType").equals("0x80000000")){
							if(NTFSDefinitions.StandardAttributeResidentLayout.GetFieldValueNumericUnsigned("NonResidentFlag") != 0){
								_fis.getChannel().position(NTFSDefinitions.StandardAttributeResidentLayout.fileStartPosition);
								NTFSDefinitions.StandardAttributeNonResidentLayout.ReadData(_fis);
								System.out.println();
								NTFSDefinitions.StandardAttributeNonResidentLayout.ShowLayout();
							}
						}
						iCount++;
						if(iCount > 10) break;
					}
				}
				_fis.getChannel().position(NTFSDefinitions.FileRecordLayout.fileStartPosition + 1024);
			}
			
			//Close HD image file
			_fis.close();
			
		}catch(Exception ex){
			System.out.println(ex.toString());
		}		
	}
	
	/***
	 * Read the in memory buffer for master file entries
	 * @param iBytesRead
	 * @return the offset in the buffer where the entry is found
	 */
	public static int searchBuffer(int iBytesRead){
		for(int i=0; i<iBytesRead; i++){
			if(_buffer[i] == 0x46){
				if((iBytesRead - i) < 1024) return i;
				if(_buffer[i+1] == 0x49 && 
				   _buffer[i+2] == 0x4c &&
				   _buffer[i+3] == 0x45 &&
				   _buffer[i+4] == 0x30){
					return i;
				}
			}
		}
		return iBytesRead;
	}
	
	public static void usage(){
		System.out.println("Usage: NTFScavenger [options] -input [imagefile]");
		System.out.println("Options:");
		System.out.println("\t-input\tThe image file to scavenge.");
		System.out.println("\t-offset\tThe offset to start reading at.");
	}

}
