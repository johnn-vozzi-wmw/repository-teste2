package br.com.wmw.lavenderepda.business.conf;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Hashtable;
import totalcross.util.Vector;


public class ClienteRedeLiberadoComSenhaConfig {

	public static Hashtable clienteRedeAtrasado = new Hashtable(0);

	public static String prepareValues() {
		StringBuffer stringBuffer = new StringBuffer();
		String currentDateKey = DateUtil.getCurrentDateYYYYMMDD();
		if (ValueUtil.isNotEmpty(clienteRedeAtrasado)) {
			Vector keys = clienteRedeAtrasado.getKeys();
			Vector values = clienteRedeAtrasado.getValues();
			int size = values.size();
			for (int i = 0; i < size; i++) {
				if (ValueUtil.valueNotEqualsIfNotNull(currentDateKey, keys.elementAt(i))) {
					continue;
				}
				String[] protocol = (String[]) values.items[i];
				for (int j = 0; j < protocol.length; j++) {
					stringBuffer.append(protocol[j]);
					if (!(j == (protocol.length - 1))) {
						stringBuffer.append(UiUtil.defaultSeparatorPairValue);
					}
				}
				if (!(i == (size - 1))) {
					stringBuffer.append(UiUtil.defaultSeparatorValue);
				}
			}
		}
		//--
		return stringBuffer.toString();
	}

	public static void splitPairValueAndSetAtributes(String value) {
		clienteRedeAtrasado = StringUtil.splitIndexAndValues(value);
	}

	/**
	 * Verifica cliente está liberado com senha na data atual
	 * @param cdRede 
	 * @return Boolean indicando se o cliente está liberado com senha na data atual.
	 * */
	public static boolean isClienteRedeLiberadoComSenha(String cdRede) {
		String key = DateUtil.getCurrentDateYYYYMMDD();
		return isClienteRedeLiberadoComSenha(cdRede, key);
	}

	/**
	 * Verifica cliente está liberado com senha na data atual
	 * @return Boolean indicando se o cliente está liberado com senha na data atual.
	 * */
	protected static boolean isClienteRedeLiberadoComSenha(String cdRede, String key) {
		if (ValueUtil.isNotEmpty(clienteRedeAtrasado) && ValueUtil.isNotEmpty(cdRede)) {
			String[] arrayValores = (String[]) clienteRedeAtrasado.get(key);
			if (ValueUtil.isNotEmpty(arrayValores)) {
				return new Vector(arrayValores).contains(cdRede);
			}
		}
		return false;
	}

	public static void put(String cdRede) {
		String dateYYYYMMDD = DateUtil.getCurrentDateYYYYMMDD();
		put(cdRede, dateYYYYMMDD);
	}

	protected static void put(String cdRede, String key) {
		if (ValueUtil.isNotEmpty(cdRede)) {
			String[] arrayValores = (String[]) clienteRedeAtrasado.get(key);
			if (ValueUtil.isEmpty(arrayValores)) {
				arrayValores = new String[] {key};
			}
			Vector v = new Vector(arrayValores);
			v.addElement(cdRede);
			clienteRedeAtrasado.put(key, v.toObjectArray());
		}
	}

}