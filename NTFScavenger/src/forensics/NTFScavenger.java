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
	private static java.io.FileInputStream _fis;
	
	public static void main(String[] args) {
		if(args.length != 1) {
			usage();
			return;
		}
		
		try{		
			for(int i=0; i<args.length; i++){
				if(args[i].toLowerCase().equals("-input")){
					_fileName = args[i+1];
				}
			}
			
			//Open the input file
			_fis = new java.io.FileInputStream(_fileName);
			
			//Read Boot Sector
			NTFSDefinitions.BootSectorLayout.ReadData(_fis, 1024);
			
		}catch(Exception ex){
			System.out.println(ex.toString());
		}		
	}
	
	public static void usage(){
		System.out.println("Usage: NTFScavenger [options] -input [imagefile]");
		System.out.println("Options:");
		System.out.println("\t-boot\tShow the boot sector information");
		System.out.println("\t-input\tThe image file to scavenge.");
		System.out.println("\t-sector\tThe size in bytes of a sector");
	}

}
