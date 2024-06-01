package br.com.wmw.lavenderepda;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import br.com.wmw.framework.business.service.MensagemExcecService;

public class MessagesTestCase  {

	@Test
	public void testStringConstantsWithoutFinalModifier() {
		Field[] fields = getDeclaredFields();
		for (Field field : fields) {
			if (!field.getType().isPrimitive() && field.getType() == String.class) {
				assertTrue(!Modifier.isFinal(field.getModifiers()), "Váriavel não tipo \'String' não pode ser \'final\'");
			}
		}
	}

	private Field[] getDeclaredFields() {
		Field[] fields = new Field[0];
		try {
			fields = MensagemExcecService.getInstance().getDeclaredFields(Class.forName(Messages.class.getName()));
		} catch (Exception ex) {
			//--
		}
		return fields;
	}
			
}
