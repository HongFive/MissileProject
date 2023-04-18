package edu.szu.enums;

public enum NodeStatesEnum {
	
	WORKING(1),		// 发布成功
	EMPTY(0);		// 禁止播放，管理员操作
	
	public final int value;

	NodeStatesEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
