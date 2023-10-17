package br.com.nataliafc.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
	
	// Permite ao usuário mesclar propriedades não nulas de dois objetos.
	public static void copyNonNullProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target, getNullPropertyName(source));
	}

	//neste método, vou coletar todas as propriedades nulas do objeto que vou alterar através do put
	// depois, vou copiar e jogar dentro do método de cima, para mesclar as propriedades não nulas
	public static String[] getNullPropertyName(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);

		PropertyDescriptor[] propertyDescriptors = src.getPropertyDescriptors();
		
		Set<String> emptyNames = new HashSet<>();
		
		// para cada descritor de propriedade que existe no meu propertydescriptors, eu vou dar um get nos nomes
		// se forem nulos, vou adicionar no emptyNames
		for (PropertyDescriptor pd : propertyDescriptors) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if(srcValue ==  null) {
				emptyNames.add(pd.getName());
			}
		}
		
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

}
