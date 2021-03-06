package forensics;

public class DiskLayout {
	public byte[] data;//The whole data section
	private java.lang.Object[] _layout;//Name,Position,Length
	private int _layoutPointer = 0;
	public byte[] buffer;
	private java.io.ByteArrayInputStream _tempStream;
	public long fileStartPosition = 0;
	
	/****
	 * Holds an area of data and provides a layout for that data
	 * @param size Total size of area in bytes
	 * @param numberOfFields Number of fields that will be represented by this data
	 */
	public DiskLayout(int size, int numberOfFields){
		this.data = new byte[size];
		this.buffer = new byte[1024*1024];
		this._tempStream = new java.io.ByteArrayInputStream(data);
		this._layout = new Object[numberOfFields*3];
	}
	
	public int GetLayoutLength(){
		return this.data.length;
	}
	
	/****
	 * Reads in a section of data
	 * @param i The input stream to read from
	 */
	public void ReadData(java.io.FileInputStream i){
		try{
			this.fileStartPosition = i.getChannel().position();
			i.read(data, 0, this.data.length);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/****
	 * Adds a field to the layout of the data
	 * @param Name Field Name
	 * @param Position The position inside of the data
	 * @param Length The length of the field
	 */
	public void AddLayoutField(String Name, int Position, int Length){
		this._layout[_layoutPointer++] = Name;
		this._layout[_layoutPointer++] = Position;
		this._layout[_layoutPointer++] = Length;
	}
	
	/****
	 * Returns the position of the Field 
	 * @param Name Field Name
	 * @return Field position within the data
	 */
	public int GetFieldPosition(String Name){
		int pos = 0;
		for(int i=0; i<_layout.length; i+=3){
			if(((String)_layout[i]).equals(Name)){
				pos = (int)_layout[i+1];
				break;
			}
		}
		
		return pos;
	}
	
	/****
	 * Returns the field length
	 * @param Name Field Name
	 * @return Field length
	 */
	public int GetFieldLength(String Name){
		int length = 0;
		for(int i=0; i<this._layout.length; i+=3){
			if(((String)this._layout[i]).equals(Name)){
				length = (int)this._layout[i+2];
				break;
			}
		}
		return length;
	}
	
	/**********
	 * Gets the value of the field in raw bytes
	 * @param Name Field Name
	 * @return The length of the value in Bytes that can be retrieved from the
	 *         beginning of the buffer
	 */
	public int GetFieldValueRawBuffer(String Name){
		int iPosition = 0;
		int iLength = 0;
		for(int i=0; i<this._layout.length; i+=3){
			if(((String)this._layout[i]).equals(Name)) {
				iPosition = (int)this._layout[i+1];
				iLength = (int)this._layout[i+2];
			}
		}
		this._tempStream.reset();
		this._tempStream.skip(iPosition);
		this._tempStream.read(this.buffer, 0, iLength);
		
		return iLength;
	}
	
	/********
	 * Gets a numeric value from the field returned as a long
	 * @param Name Field Name
	 * @return The numeric value as a long
	 */
	public long GetFieldValueNumeric(String Name){
		long value = 0;
		
		int length = GetFieldValueRawBuffer(Name);
		if(length != 8 && length != 4 && length != 2 && length != 1){
			System.out.println("Invalid numeric length for " + Name + " length of " + length);
			return 0;
		}
		
		for(int i=length-1; i>=0; i--){
			value = (value << 8) | ((char)this.buffer[i] & 0x00FF);
		}
		if (length == 1){
			value = (value << 56) >> 56;
		}
		if (length == 2){
			value = (value << 48) >> 48;
		}
		if (length == 4){
			value = (value << 32) >> 32;
		}
		
		return value;
	}
	
	/*************
	 * Get a numeric unsigned value (This will not work if the number of bytes is 8)
	 * @param Name Field Name
	 * @return The numeric value as long
	 */
	public long GetFieldValueNumericUnsigned(String Name){
		long value = 0;
		
		int length = GetFieldValueRawBuffer(Name);
		if(length != 8 && length != 4 && length != 2 && length != 1){
			System.out.println("Invalid numeric length for " + Name + " length of " + length);
			return 0;
		}
		
		for(int i=length-1; i>=0; i--){
			value = (value << 8) | (char)this.buffer[i] & 0x00FF;
		}
		
		return value;
	}
	
	/**********
	 * Displays an array of bytes in raw hex notation
	 * @param b The byte array
	 * @param length The number of bytes within the array to read
	 * @return String with each byte shown in hex format
	 */
	public String showRaw(String Name){
		int length = GetFieldValueRawBuffer(Name);
		String s = "0x";
		for(int i=0; i<length; i++){
			s += String.format("%02X", this.buffer[i]);
		}
		return s;
	}
	
	public void ShowLayout(){
		for(int i=0; i<this._layout.length; i+=3){
			System.out.println(((String)this._layout[i]) + " = " + GetFieldValueNumericUnsigned((String)this._layout[i]) + " = " + showRaw((String)this._layout[i]));
		}
	}
}
