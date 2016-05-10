package forensics;

public class NTFSDefinitions {
	//**************************************
	//Boot Sector Layout
	//**************************************
	public static DiskLayout BootSectorLayout = new DiskLayout(0x55,10);
	public static DiskLayout FileRecordLayout = new DiskLayout(0x30,14);
	public static DiskLayout StandardAttributeResidentLayout = new DiskLayout(0x19,11);
	public static DiskLayout StandardAttributeNonResidentLayout = new DiskLayout(0x40,15);
	
	public static void init(){
		//Boot sector fields for BPB
		NTFSDefinitions.BootSectorLayout.AddLayoutField("Beginning", 0x00, 11);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("BytesPerSector", 0x0B, 2);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("SectorsPerCluster", 0x0D, 1);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("ReservedSectors", 0x0E, 2);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("MediaDescriptor", 0x15, 1);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("TotalSectors", 0x28, 8);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("LCN_MFT", 0x30, 8);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("LCN_MFTMIRR", 0x38, 8);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("ClustersPerMFT_Rec", 0x40, 1);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("ClustersPerIndexBuffer", 0x44, 1);
		NTFSDefinitions.BootSectorLayout.AddLayoutField("VolumeSerialNumber", 0x48, 8);
		
		//Represents the fixed portion of the file record layout
		NTFSDefinitions.FileRecordLayout.AddLayoutField("MagicNumber", 0x00, 4);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("OffsetToUpdateSequence", 0x04, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("SizeInWordsUpdateSequenceArray", 0x06, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("LogfileSequenceNumber", 0x08, 8);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("SequenceNumber", 0x10, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("HardLinkCount", 0x12, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("OffsetToFirstAttribute", 0x14, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("Flags", 0x16, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("RealSizeOfFileRecord", 0x18, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("AllocatedSizeOfFileRecord", 0x1C, 4);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("FileReferenceToBaseFileRecord", 0x20, 8);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("NextAttributeId", 0x28, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("Align4ByteBoundary", 0x2A, 2);
		NTFSDefinitions.FileRecordLayout.AddLayoutField("NumberOfThisMFT_Record", 0x2C, 4);
		
		//The standard attribute header resident fixed portion
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("AttributeType", 0x00, 4);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("Length", 0x04, 4);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("NonResidentFlag", 0x08, 1);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("NameLength", 0x09, 1);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("OffsetToName", 0x0A, 2);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("Flags", 0x0C, 2);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("AttributeId", 0x0E, 2);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("LengthOfTheAttribute", 0x10, 4);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("OffsetToTheAttribute", 0x14, 2);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("IndexedFlag", 0x16, 1);
		NTFSDefinitions.StandardAttributeResidentLayout.AddLayoutField("Padding", 0x17, 1);
		
		//The standard attribute header non-resident fixed portion
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("AttributeType", 0x00, 4);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("Length", 0x04, 4);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("NonResidentFlag", 0x08, 1);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("NameLength", 0x09, 1);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("OffsetToName", 0x0A, 2);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("Flags", 0x0C, 2);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("AttributeId", 0x0E, 2);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("StartingVCN", 0x10, 8);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("LastVCN", 0x18, 8);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("OffsetToDataRuns", 0x20, 2);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("CompressionUnitSize", 0x22, 2);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("Padding", 0x24, 4);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("AllocatedSizeOfAttribute", 0x28, 8);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("RealSizeOfAttribute", 0x30, 8);
		NTFSDefinitions.StandardAttributeNonResidentLayout.AddLayoutField("InitializedDataSizeOfStream", 0x38, 8);
	}
}
