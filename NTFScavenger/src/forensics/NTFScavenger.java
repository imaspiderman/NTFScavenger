package forensics;

public class NTFScavenger {

	/**************************************
	 * This program is designed to try and pull files off a damaged NTFS file system
	 * by using the MFT records that can be scavenged from a hard drive image.
	 * At no point does this program attempt to write to the file it is pulling data from.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	public static void usage(){
		System.out.println("Usage: NTFScavenger [options] -input [imagefile]");
		System.out.println("Options:");
		System.out.println("\t-input\tThe image file to scavenge.");
		System.out.println("\t-offset\tThe offset to start at in bytes.");
		System.out.println("\t-bootsector\tShow boot sector information.");
	}

}
