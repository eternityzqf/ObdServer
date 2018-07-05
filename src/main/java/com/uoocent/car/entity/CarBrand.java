package com.uoocent.car.entity;
/**   
 * @author banji   
 * @date 2017年12月18日 上午9:31:13 
 */
public class CarBrand {
	private String value;
	private String text;

	public CarBrand(){
		
	}
	public CarBrand(String value, String text) {
		super();
		this.value = value;
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
