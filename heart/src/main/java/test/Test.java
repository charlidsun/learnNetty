package test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {

	public static void main(String[] args) {
		Map<String,Object> map = new HashMap<>();
		map.put("1", "aaa");
		map.put("2", "bbb");
		map.put("3", "ccc");
		map.put("4", "ddd");
		//mapOper(map, "ddd");
		myFor(map);
	}
	
	//过滤
	public static void mapOper(Map<String,Object> map,String val) {
		
		String result = "";
		System.out.println("java 8 befor");
		for (Map.Entry<String, Object> obj : map.entrySet()) {
			if (val.equals(obj.getValue())) {
				System.out.println("find it "+ obj.getKey());
			}
		}
		
		System.out.println("java 8 after");

		map.entrySet().stream().filter(maps -> val.equals(maps.getValue())).forEach(maps->map.remove(maps.getKey()));
		
	}
	
	//循环map
	public static void myFor(Map<String,Object> map) {
		for (Map.Entry<String, Object> ma : map.entrySet()) {
			System.out.println(ma.getKey() +"----"+ma.getValue());
		}
		
		map.entrySet().stream().forEach(ma -> System.err.println(ma.getKey()+"----"+ma.getValue()));
	}
	
}
