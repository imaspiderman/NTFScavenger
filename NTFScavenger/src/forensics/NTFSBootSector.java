package forensics;

/************************************************
 * Offset    Length     Description
 * **********************************************
 * 0x00      3 bytes    Jump instruction
 * 0x03      8 bytes    OEM ID
 * 0x0B      25 bytes   BPB
 * 0x24      48 bytes   Extended BPB
 * 0x54      426 bytes  Bootstrap code
 * 0x01FE    2 bytes    End of sector marker (0x55AA)
 * 
 * BPB Layout
 * Note: All numeric values are in little endian format
 * **********************************************
 * Offset    Length     Name / Description
 * **********************************************
 * 0x0B      2 bytes    Bytes Per Sector (Usually 00 02) or 512
 * 0x0D      1 byte     Sectors Per Cluster
 * 0x0E      2 bytes    Reserved Sectors. Always 0 because NTFS places the boot sector 
 *                      at the beginning of the partition. 
 *                      If the value is not 0, NTFS fails to mount the volume.
 * 0x10      3 bytes    Value must be 0 or NTFS fails to mount the volume.
 * 0x13      2 bytes    Value must be 0 or NTFS fails to mount the volume.
 * 0x15      1 byte     Media Descriptor. Provides information about the media being used. 
 *                      A value of F8 indicates a hard disk and F0 indicates a high-density 
 *                      3.5-inch floppy disk. Media descriptor entries are a legacy of 
 *                      MS-DOS FAT16 disks and are not used in Windows Server 2003.
 * 0x16      2 bytes    Value must be 0 or NTFS fails to mount the volume.
 * 0x18      2 bytes    Not used or checked by NTFS.
 * 0x1A      2 bytes    Not used or checked by NTFS.
 * 0x1C      4 bytes    Not used or checked by NTFS.
 * 0x20      4 bytes    The value must be 0 or NTFS fails to mount the volume.
 * 0x24      4 bytes    Not used or checked by NTFS.
 * 0x28      8 bytes    Total Sectors. The total number of sectors on the hard disk.
 * 0x30      8 bytes    Logical Cluster Number for the File $MFT. Identifies the 
 *                      location of the MFT by using its logical cluster number.
 * 0x38      8 bytes    Logical Cluster Number for the File $MFTMirr. Identifies 
 *                      the location of the mirrored copy of the MFT by using its 
 *                      logical cluster number.
 * 0x40      1 byte     Clusters Per MFT Record. The size of each record. NTFS creates 
 *                      a file record for each file and a folder record for each folder 
 *                      that is created on an NTFS volume. Files and folders smaller 
 *                      than this size are contained within the MFT. If this number is 
 *                      positive (up to 7F), then it represents clusters per MFT record. 
 *                      If the number is negative (80 to FF), then the size of the file 
 *                      record is 2 raised to the absolute value of this number.
 * 0x41      3 bytes    Not used by NTFS.
 * 0x44      1 byte     Clusters Per Index Buffer. The size of each index buffer, which 
 *                      is used to allocate space for directories. If this number is 
 *                      positive (up to 7F), then it represents clusters per MFT record. 
 *                      If the number is negative (80 to FF), then the size of the file 
 *                      record is 2 raised to the absolute value of this number.
 * 0x45      3 bytes    Not used by NTFS.
 * 0x48      8 bytes    Volume Serial Number. The volume’s serial number.
 * 0x50      4 bytes    Not used by NTFS.
 */
public class NTFSBootSector {

	private java.util.LinkedList<NTFSField>_bootSectorFields;
	
	/************
	 * Creates a field list of the BPB section of the boot sector
	 * which is the only part this program really cares about.
	 */
	public NTFSBootSector(){
		_bootSectorFields = new java.util.LinkedList<NTFSField>();
		_bootSectorFields.add(new NTFSField("Bytes Per Sector", 2, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Sectors Per Cluster", 1, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Reserved Sectors", 2, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Zero Field", 3, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Zero Field", 2, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Media Descriptor", 1, NTFSField.DataType.RAW));
		_bootSectorFields.add(new NTFSField("Zero Field", 2, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Unused", 2, NTFSField.DataType.RAW));
		_bootSectorFields.add(new NTFSField("Unused", 2, NTFSField.DataType.RAW));
		_bootSectorFields.add(new NTFSField("Unused", 4, NTFSField.DataType.RAW));
		_bootSectorFields.add(new NTFSField("Zero Field", 4, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Unused", 4, NTFSField.DataType.RAW));
		_bootSectorFields.add(new NTFSField("Total Sectors", 8, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Logical MFT Cluster", 8, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Logical MFTMirr Cluster", 8, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Clusters Per MFT Record", 1, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Unused", 3, NTFSField.DataType.RAW));
		_bootSectorFields.add(new NTFSField("Clusters Per Index Buffer", 1, NTFSField.DataType.NUMERIC));
		_bootSectorFields.add(new NTFSField("Unused", 3, NTFSField.DataType.RAW));
		_bootSectorFields.add(new NTFSField("Volume Serial Number", 8, NTFSField.DataType.RAW));
		_bootSectorFields.add(new NTFSField("Unused", 4, NTFSField.DataType.RAW));
	}
	
	/**************
	 * Gets the field list as a linked list
	 * @return
	 */
	public java.util.LinkedList<NTFSField> getFields(){
		return this._bootSectorFields;
	}
}
