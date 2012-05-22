package org.utopia.common.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ClassUtil {

	/**
	 * Try to cast an object into an specific class 
	 * @param destiny class type
	 * @param source object to be converted
	 * @return object casted to the class
	 */
	public static Object cast(Class<?> destiny, Object source) {
		
		Object casted = source;
	
		Class<?> srcClass = null;
		
		if (source != null) { srcClass = source.getClass(); }
		
		if (srcClass != null && ! destiny.equals(srcClass)) {
			
			// Boolean
			if (destiny.equals(Boolean.class) || destiny.equals(boolean.class)) {				
				casted = ( "T".equals(source) 
						|| "t".equals(source) 
						|| "Y".equals(source) 
						|| "y".equals(source) 
						|| "S".equals(source) 
						|| "s".equals(source)
						|| "1".equals(source.toString())
						);

			// Numbers - efreet usually gets numbers as bigdecimals
			} else if ((destiny.equals(Byte.class) || destiny.equals(byte.class)) && (srcClass.equals(BigDecimal.class)) ) {
				casted = ((BigDecimal) source).byteValue();

			} else if ((destiny.equals(Short.class) || destiny.equals(short.class)) && (srcClass.equals(BigDecimal.class)) ) {
				casted = ((BigDecimal) source).shortValue();

			} else if ((destiny.equals(Integer.class) || destiny.equals(int.class)) && (srcClass.equals(BigDecimal.class)) ) {
				casted = ((BigDecimal) source).intValue();

			} else if ((destiny.equals(Long.class) || destiny.equals(long.class)) && (srcClass.equals(BigDecimal.class)) ) {
				casted = ((BigDecimal) source).longValue();

			} else if ((destiny.equals(Float.class) || destiny.equals(float.class)) && (srcClass.equals(BigDecimal.class)) ) {
				casted = ((BigDecimal) source).floatValue();

			} else if ((destiny.equals(Double.class) || destiny.equals(double.class)) && (srcClass.equals(BigDecimal.class)) ) {
				casted = ((BigDecimal) source).doubleValue();

			} else if ((destiny.equals(Character.class) || destiny.equals(char.class)) && (srcClass.equals(BigDecimal.class)) ) {
				casted = ((BigDecimal) source).shortValue();
			// Numeric - interchangeable classes
			} else if ((destiny.equals(byte.class)) && (srcClass.equals(Byte.class))) {
				casted = ((Byte) source).byteValue();
			} else if ((destiny.equals(short.class)) && (srcClass.equals(Short.class))) {
				casted = ((Short) source).shortValue();
			} else if ((destiny.equals(int.class)) && (srcClass.equals(Integer.class))) {
				casted = ((Integer) source).intValue();
			} else if ((destiny.equals(long.class)) && (srcClass.equals(Long.class))) {
				casted = ((Long) source).longValue();
			} else if ((destiny.equals(float.class)) && (srcClass.equals(Float.class))) {
				casted = ((Float) source).floatValue();
			} else if ((destiny.equals(double.class)) && (srcClass.equals(Double.class))) {
				casted = ((Double) source).doubleValue();
			} else if ((destiny.equals(char.class)) && (srcClass.equals(Character.class))) {
				casted = ((Character) source).charValue();
			// String to numbers
			} else if ((destiny.equals(Byte.class) || destiny.equals(byte.class)
					 || destiny.equals(Short.class) || destiny.equals(short.class)
					 || destiny.equals(Integer.class) || destiny.equals(int.class)
					 || destiny.equals(Long.class) || destiny.equals(long.class)
					 || destiny.equals(Float.class) || destiny.equals(float.class)
					 || destiny.equals(Double.class) || destiny.equals(double.class)) 
					 && (srcClass.equals(String.class)) 					
					 ) {
				casted = cast(destiny, (source != null)?new BigDecimal(source.toString()):new BigDecimal(0) );
				
			} else if ((destiny.equals(Character.class) || destiny.equals(char.class)) && (srcClass.equals(String.class)) ) {
				casted = source.toString().charAt(0);
				
			} else if ((destiny.equals(Integer.class)) && srcClass.equals(int.class) ) {
				casted = new Integer((Integer) source);
			
			} else if ((destiny.equals(int.class)) && srcClass.equals(Integer.class) ) {
				casted = ((Integer) source).intValue();
			
			// String
			} else if (destiny.equals(String.class)) {
				casted = source.toString();

				
			// enum
			} else if (destiny.isEnum()) {
				if (srcClass.equals (int.class) || srcClass.equals(Integer.class) ) {
					casted = destiny.getEnumConstants()[((Integer) source)];
				} else
				if (srcClass.equals(BigDecimal.class)) {
					casted = destiny.getEnumConstants()[((BigDecimal) source).intValue()];
				} else
				if (srcClass.equals(String.class) || srcClass.equals(Character.class) || srcClass.equals(char.class)) {
					Object[] enumArray = destiny.getEnumConstants();
					for (Object element : enumArray) {
						if (element.toString().startsWith(source.toString())) {
							casted = element;
							break;
						}
					}
				}
				
				
			// TODO - complete with specific conversions if needed
			} else {
			
				// Other
				casted = destiny.cast(source);
			}
		}
		
		return casted;		
	}

	/**
	 * Returns a Field for an specific field, trying to find the private/public in
	 * the class, or recursively the public field.
	 * @param parent Start Class to search
	 * @param fieldName field name to search
	 * @return a field
	 * @throws Exception error
	 */
	public static Field getField(Class<?> parent, String fieldName) throws Exception {
		
		Field f = null;
		
		if (parent == null) throw new Exception("Class definition is null");
		if (fieldName == null) throw new Exception("Field name is null");
		
		try {
			f = parent.getDeclaredField(fieldName);
		} catch (Exception e1) {
			try {
				f = parent.getField(fieldName);
			} catch (Exception e2) {
				throw new Exception("Field " + fieldName + " not found in " + parent.getName(), e2);
			}
		}
		
		return f;
	}
	
	/**
	 * Returns all the declared fields (private/public) from a class, and all the public fields from
	 * it's parent classes
	 * @param parent class
	 * @return Array of Fields
	 * @throws Exception error
	 */
	public static Field[] getFields(Class<?> parent) throws Exception {
		
		Field[] f1 = null;
		Field[] f2 = null;

		if (parent == null) throw new Exception("Class definition is null");

		f1 = parent.getDeclaredFields();
		f2 = parent.getFields();
		if (f2 != null && f2.length > 0) {
			if (f1 == null || f1.length <= 0) {
				f1 = f2;
			} else {
				ArrayList<Field> f3 = new ArrayList<Field>();
				for (Field f : f1 ) { f3.add(f); }
				for (Field f : f2 ) { f3.add(f); }
				f1 = f3.toArray(f1);
			}
		}

		return f1;
	}
}
