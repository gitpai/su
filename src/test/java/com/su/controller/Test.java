package com.su.controller;

import com.su.util.Md5_1;

public class Test {
	public static void main(String[] args) {
		/*byte a=(byte) 0x5f;
		byte b = (byte) 0xff;*/
		
	/*	byte[]uuid={(byte) 0x91,(byte)0xce,(byte)0xe0,(byte)0xdd,(byte)0x97,(byte)0x93,0x31,0x0a,(byte)0xc4,(byte)0xcd,0x1d,(byte)0xdc,0x13,0x3a,0x00,0x35};
		*/
		byte[]  uuid=stringToByte("91cee0dd9793330ac4cd1ddc133a0035");
		String a =byteToString(uuid);
		System.out.println(a);
		System.out.println("91cee0dd9793310ac4cd1ddc133a0035");
	}
	public  static byte[] stringToByte(String uuid){
		
		char [] dataChar=uuid.toCharArray();
		byte [] dataByte=new byte[dataChar.length/2];
	//	System.out.println(dataChar.length/2);
		for(int i=0;i<dataChar.length;i+=2){			
			byte high,low;//临时变量，保存ch的高4位、低四位
				//保存转换后的字符，	
			if(dataChar[i]>'9'){	
				high=(byte) (dataChar[i]-'a');
				high=(byte) (high+0x0a);
				System.out.println(high);
			}else{
				dataChar[i]=(char) (dataChar[i]-'0');
				high=(byte) (dataChar[i]);
				}
				
			if(dataChar[i+1]>'9'){	
					low=(byte) (dataChar[i+1]-'a');
					low=(byte) (low+0x0a);	
					System.out.println(low);
			}else{
					dataChar[i+1]=(char) (dataChar[i+1]-'0');
					low=(byte) (dataChar[i+1]);	
				
			}					
			dataByte[(i+1)/2]=(byte) ((high<<4)+low);	
			//System.out.println(i);
		}	
		return  dataByte;
	}
			
			//if(dataChar[i+1]>'9')
			/*high=(byte) ((0x0f)&(uuid[i]>>4));
			low=(byte) ((0x0f)&uuid[i]);
			//高4位保存在datatxt[0]中
			if(high<=(0x09))
				datatxt[i]=(char) (high+'0');
			else
				datatxt[i]=(char) (high-0x0a+'a');
			//System.out.print(datatxt[i]);
			sb.append(datatxt[i]);
			//低4位保存在datatxt[1]中
			if(low<=(0x09))
				datatxt[i+1]=(char) (low+'0');
			else
				datatxt[i+1]=(char) (low-0x0a+'a');
			sb.append(datatxt[i+1]);
		}*/
	
	public  static String byteToString(byte[] uuid){
		char [] datatxt=new char[uuid.length*2];
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<uuid.length;i++){			
			byte high,low;//临时变量，保存ch的高4位、低四位
				//保存转换后的字符，			
			high=(byte) ((0x0f)&(uuid[i]>>4));
			low=(byte) ((0x0f)&uuid[i]);
			//高4位保存在datatxt[0]中
			if(high<=(0x09))
				datatxt[i]=(char) (high+'0');
			else
				datatxt[i]=(char) (high-0x0a+'a');
			//System.out.print(datatxt[i]);
			sb.append(datatxt[i]);
			//低4位保存在datatxt[1]中
			if(low<=(0x09))
				datatxt[i+1]=(char) (low+'0');
			else
				datatxt[i+1]=(char) (low-0x0a+'a');
			sb.append(datatxt[i+1]);
		}
		/*for(int i=0;i<datatxt.length;i++){
			System.out.print(datatxt[i]);
		}*/
		return sb.toString();
		
	}
}
