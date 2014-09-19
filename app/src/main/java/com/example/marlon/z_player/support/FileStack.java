package com.example.marlon.z_player.support;

import java.io.File;

public class FileStack {
	
	private File[] stack;
	private  int top=0;
	
	public FileStack(){
		stack= new File[10];
	}
	
	private void resize(){
		int size= stack.length*2;
		File[] temp =new File[size];
		for (int i = 0; i < stack.length; i++) {
			temp[i]=stack[i];
		}
		
		stack= temp;
	};
	public void push (File f){
		
		top++;
		if (top >stack.length){resize();}
		stack[top]=f;
	}
	public File pop(){
		
		if (!isEmpty()) {
			File item = stack[top];
			stack[top] = null;
			top--;
			return item;
		}
		else {return null;}
	}
	
	public File peek(){
		return stack[top];
	}
	public boolean isEmpty(){
		if (stack.length<1){return true;}
		else {return false;}
	}
}
