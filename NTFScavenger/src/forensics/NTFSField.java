package forensics;
/***********************************************
 * This class will represent a field that is read
 * from the raw hard drive image
 * 
 * @author Greg
 *
 */
public class NTFSField {
	public String Name;
	private byte[] _data;
	private int _dataSize;
	public enum DataType{NUMERIC,STRING,RAW};
	private DataType _type;
	
	/**********
	 * Creates a new NTFSField with a friendly name and the size in bytes
	 * @param NameIn
	 * @param DataSize
	 */
	public NTFSField(String NameIn, int DataSize, DataType t){
		Name = NameIn;
		_data = new byte[DataSize];
		_type = t;
	}
	
	/**********
	 * Gets the data type of the field
	 * @return
	 */
	public DataType getType(){
		return _type;
	}
	
	/************
	 * Gets the size of the Data byte array
	 * @return
	 */
	public int getFieldSize(){
		return _dataSize;
	}
	
	/***********
	 * Converts the Data byte array into a long value
	 * @return
	 */
	public long getNumericValue(){
		long returnVal = 0;
		for(int i=_data.length; i>0; i--){
			returnVal |= _data.length;
			if(i-1 > 0) returnVal <<= 8;
		}
		
		return returnVal;
	}
	
	/**********
	 * Returns the bytes of the Data
	 * @return
	 */
	public byte[] getBinaryValue(){
		return _data;
	}
	
	/**********
	 * Converts the bytes in the Data to characters assuming 2 bytes per character
	 * @return
	 */
	public String getStringValue(){
		String sValue = "";
		for(int i=0; i<_data.length; i+=2){
			sValue += (char)_data[i];
		}
		
		return sValue;
	}

	@Override
	public String toString() {		
		String sValue = "";
		if(this._type.equals(DataType.NUMERIC)){
			sValue = String.format("%1d", getNumericValue());
		}
		if(this._type.equals(DataType.STRING)){
			sValue = getStringValue();
		}
		if(this._type.equals(DataType.RAW)){
			for(int i=0; i<_data.length; i++){
				sValue += String.format("%02x", _data[i]);
			}
		}
		
		return sValue;
	}
	
	
}
