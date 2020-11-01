package com.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bean.Address;

import java.util.Set;

public class Test {

	public static void main(String[] args) {
		DBUtil db = new DBUtil();
		String sql = "select * from address";
		List<Address> selects = db.select(sql, null, Address.class);
		Object[] objs = selects.toArray();
		for (Object object : objs) {
			System.out.println(object);
		}
		db.close();
		//System.out.println(test(Student.class));
		
	}
}
