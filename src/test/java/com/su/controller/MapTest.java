package com.su.controller;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MapTest {
public static void main(String[] args) {
	 ScriptEngineManager manager = new ScriptEngineManager();
	  ScriptEngine engine = manager.getEngineByName("javascript");
	     try{    

	          engine.eval("var a=3; var b=4;print (a+b);");

	         // engine.eval("alert(\"js alert\");");    // ���ܵ���������ж����js���� // ���󣬻��׳�alert���ò����ڵ��쳣
	 }catch(ScriptException e){

	 e.printStackTrace();
	 }
	 }   
}
	 
