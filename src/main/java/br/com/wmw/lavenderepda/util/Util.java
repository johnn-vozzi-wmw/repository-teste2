package br.com.wmw.lavenderepda.util;

import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import totalcross.io.File;
import totalcross.ui.font.FontMetrics;
import totalcross.ui.image.Image;

public class Util {
	
	public static Image NOIMAGE;
	public static Image LOADINGIMAGE;

	public static Map<Integer, String> mapUnidade;
	public static Map<Integer, String> mapDezena;
	public static Map<Integer, String> mapCentena;
	
	public static String getQtItemPedidoFormatted(double qtItemPedido) {
		if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
			return StringUtil.getStringValueToInterface(qtItemPedido, 0);
		} else {
			return StringUtil.getStringValueToInterface(qtItemPedido);
		}
	}
	
	public static Image getImageForProdutoList(BaseDomain domain, int imageSize, boolean returnDefImage) {
		ProdutoBase base = (ProdutoBase) domain;
		String pathImage;
		if (base.fotoProduto != null && ValueUtil.isNotEmpty(base.fotoProduto.nmFoto)) {
			pathImage = Produto.getPathImg() + "\\";
			if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
				pathImage += SessionLavenderePda.cdEmpresa + "\\";
			}
			pathImage += base.fotoProduto.nmFoto;
		} else {
			return NOIMAGE;
		}
		try {
			File f = new File(pathImage, File.READ_ONLY);
			Image img = new Image(f);
			if (imageSize > 0) {
				return UiUtil.getSmoothScaledImageForMaxSize(img, imageSize, imageSize);
			}
			return img;
		} catch (Throwable e) {
			return returnDefImage ? NOIMAGE : null;
		}
	}
	
	public static void prepareDefaultImage(int size) {
		prepareDefaultImage(size, false);
	}
	
	public static void prepareDefaultImage(int imageSize, boolean forceReload) {
    	if (NOIMAGE == null || NOIMAGE.getWidth() != imageSize || forceReload) {
    		NOIMAGE = UiUtil.getImage("images/nophoto.jpg");
    		NOIMAGE = UiUtil.getSmoothScaledImage(NOIMAGE, imageSize, imageSize);
    	}
	}
	
	public static Image getDefaultNoImage(int size) {
		prepareDefaultImage(size);
		return NOIMAGE;
	}

	public static void prepareLoadingImage(int imageSize) {
		if (LOADINGIMAGE == null) {
			LOADINGIMAGE = UiUtil.getImage("images/loadingImage.png");
			LOADINGIMAGE = UiUtil.getSmoothScaledImage(LOADINGIMAGE, imageSize, imageSize);
		}
	}
	
	public static Image getDefaultLoadingImage(int size) {
		prepareLoadingImage(size);
		return LOADINGIMAGE;
	}
	
	public static String getValorPorExtenso(int valor) {
		String value = String.valueOf(valor);
		int size = value.length();
		StringBuffer sb = new StringBuffer(size * 5);
		char[] vl = value.toCharArray();
		int lenght = size;
		while (lenght % 3 != 0) {
			lenght++;
		}
		char[] charValue = new char[lenght];
		for (int i = size - 1; i >= 0 ; i--) {
			charValue[--lenght] = vl[i];
		}
		
		lenght = charValue.length;
		for (int j = 0; j < lenght; j += 3) {
			int cont = 0;
			loop: for (int i = j; i < j + 3; i++) {
				switch (cont) {
				case 0:
					if (ValueUtil.isNotEmpty(charValue[i]) && charValue[i] != '0') {
						if (sb.length() > 0) {
							sb.append(", ");
						}
						if (charValue[0] == '1' && ValueUtil.isNotEmpty(charValue[1]) && charValue[1] == '0' && ValueUtil.isNotEmpty(charValue[2]) && charValue[2] == '0') {
							sb.append(returnCentena(100));
						} else {
							sb.append(returnCentena(ValueUtil.getIntegerValue(String.valueOf(charValue[i]))));
						}
					}
					break;
				case 1:
					if (ValueUtil.isNotEmpty(charValue[i]) && charValue[i] != '0') {
						if (sb.length() > 0) {
							if (ValueUtil.isNotEmpty(charValue[i - 1]) && charValue[i - 1] != '0' || lenght - i < 3) {
								sb.append(" e ");
							} else {
								sb.append(", ");
							}
						}
						if (charValue[i] == '1') {
							String dezena = String.valueOf(charValue[i]) + String.valueOf(charValue[i + 1]);
							sb.append(returnDezena(ValueUtil.getIntegerValue(dezena)));
							if (lenght - j > 9) {
								sb.append(" ").append(Messages.IMPRESSAONOTAPROMISSORIA_BILHOES).append(" ");
							} else if (lenght - j > 6) {
								sb.append(" ").append(Messages.IMPRESSAONOTAPROMISSORIA_MILHOES).append(" ");
							} else if (lenght - j > 3) {
								sb.append(" ").append(Messages.IMPRESSAONOTAPROMISSORIA_MIL).append(" ");
							}
							cont++;
							break loop;
						} else {
							sb.append(returnDezena(ValueUtil.getIntegerValue(String.valueOf(charValue[i]))));
						}
					}
					break;
				case 2:
					if (ValueUtil.isNotEmpty(charValue[i]) && charValue[i] != '0') {
						if (sb.length() > 0) {
							sb.append(" e ");
						}
						sb.append(returnUnidade(ValueUtil.getIntegerValue(String.valueOf(charValue[i]))));
					}
					break;
				}
				if (lenght - i == 10) {
					if ((ValueUtil.isNotEmpty(charValue[j]) && charValue[j] != '0') || (ValueUtil.isNotEmpty(charValue[j + 1]) && charValue[j + 1] != '0') || (ValueUtil.isNotEmpty(charValue[j + 2]) && charValue[j + 2] != '0')) {
						sb.append(" ").append(Messages.IMPRESSAONOTAPROMISSORIA_BILHOES);
					} else {
						sb.append(" ").append(Messages.IMPRESSAONOTAPROMISSORIA_BILHAO);
					}
				} else if (lenght - i == 7) {
					if ((ValueUtil.isNotEmpty(charValue[j]) && charValue[j] != '0') || (ValueUtil.isNotEmpty(charValue[j + 1]) && charValue[j + 1] != '0') || (ValueUtil.isNotEmpty(charValue[j + 2]) && charValue[j + 2] != '0')) {
						sb.append(" ").append(Messages.IMPRESSAONOTAPROMISSORIA_MILHOES);
					} else {
						sb.append(" ").append(Messages.IMPRESSAONOTAPROMISSORIA_MILHAO);
					}
				} else if (lenght - i == 4) {
					sb.append(" ").append(Messages.IMPRESSAONOTAPROMISSORIA_MIL);
				}
				cont++;
			}
		}
		return sb.toString().trim();
	}
	
	private static String returnUnidade(int valor) {
		if (mapUnidade == null) {
			mapUnidade = new HashMap<>(9);
			mapUnidade.put(1, Messages.IMPRESSAONOTAPROMISSORIA_UM);
			mapUnidade.put(2, Messages.IMPRESSAONOTAPROMISSORIA_DOIS);
			mapUnidade.put(3, Messages.IMPRESSAONOTAPROMISSORIA_TRES);
			mapUnidade.put(4, Messages.IMPRESSAONOTAPROMISSORIA_QUATRO);
			mapUnidade.put(5, Messages.IMPRESSAONOTAPROMISSORIA_CINCO);
			mapUnidade.put(6, Messages.IMPRESSAONOTAPROMISSORIA_SEIS);
			mapUnidade.put(7, Messages.IMPRESSAONOTAPROMISSORIA_SETE);
			mapUnidade.put(8, Messages.IMPRESSAONOTAPROMISSORIA_OITO);
			mapUnidade.put(9, Messages.IMPRESSAONOTAPROMISSORIA_NOVE);
		}
		return mapUnidade.get(valor);
	}
	
	private static String returnDezena(int valor) {
		if (mapDezena == null) {
			mapDezena = new HashMap<>(18);
			mapDezena.put(10, Messages.IMPRESSAONOTAPROMISSORIA_DEZ);
			mapDezena.put(11, Messages.IMPRESSAONOTAPROMISSORIA_ONZE);
			mapDezena.put(12, Messages.IMPRESSAONOTAPROMISSORIA_DOZE);
			mapDezena.put(13, Messages.IMPRESSAONOTAPROMISSORIA_TREZE);
			mapDezena.put(14, Messages.IMPRESSAONOTAPROMISSORIA_CATORZE);
			mapDezena.put(15, Messages.IMPRESSAONOTAPROMISSORIA_QUINZE);
			mapDezena.put(16, Messages.IMPRESSAONOTAPROMISSORIA_DEZESSEIS);
			mapDezena.put(17, Messages.IMPRESSAONOTAPROMISSORIA_DEZESSETE);
			mapDezena.put(18, Messages.IMPRESSAONOTAPROMISSORIA_DEZOITO);
			mapDezena.put(19, Messages.IMPRESSAONOTAPROMISSORIA_DEZENOVE);
			mapDezena.put(2, Messages.IMPRESSAONOTAPROMISSORIA_VINTE);
			mapDezena.put(3, Messages.IMPRESSAONOTAPROMISSORIA_TRINTA);
			mapDezena.put(4, Messages.IMPRESSAONOTAPROMISSORIA_QUARENTA);
			mapDezena.put(5, Messages.IMPRESSAONOTAPROMISSORIA_CINQUENTA);
			mapDezena.put(6, Messages.IMPRESSAONOTAPROMISSORIA_SESSENTA);
			mapDezena.put(7, Messages.IMPRESSAONOTAPROMISSORIA_SETENTA);
			mapDezena.put(8, Messages.IMPRESSAONOTAPROMISSORIA_OITENTA);
			mapDezena.put(9, Messages.IMPRESSAONOTAPROMISSORIA_NOVENTA);
		}
		return mapDezena.get(valor);
	}
	
	private static String returnCentena(int valor) {
		if (mapCentena == null) {
			mapCentena = new HashMap<>(10);
			mapCentena.put(100, Messages.IMPRESSAONOTAPROMISSORIA_CEM);
			mapCentena.put(1, Messages.IMPRESSAONOTAPROMISSORIA_CENTO);
			mapCentena.put(2, Messages.IMPRESSAONOTAPROMISSORIA_DUZENTOS);
			mapCentena.put(3, Messages.IMPRESSAONOTAPROMISSORIA_TREZENTOS);
			mapCentena.put(4, Messages.IMPRESSAONOTAPROMISSORIA_QUATROCENTOS);
			mapCentena.put(5, Messages.IMPRESSAONOTAPROMISSORIA_QUINHENTOS);
			mapCentena.put(6, Messages.IMPRESSAONOTAPROMISSORIA_SEISCENTOS);
			mapCentena.put(7, Messages.IMPRESSAONOTAPROMISSORIA_SETECENTOS);
			mapCentena.put(8, Messages.IMPRESSAONOTAPROMISSORIA_OITOCENTOS);
			mapCentena.put(9, Messages.IMPRESSAONOTAPROMISSORIA_NOVECENTOS);
		}
		return mapCentena.get(valor);
	}
	
	public static String getIntOuDecimal(double value) {
		int parteInteira = ValueUtil.getIntegerValueTruncated(value);
		if (parteInteira > 0) {
			double resto = value % parteInteira;
			if (resto < 10E-5) {
				return String.valueOf(parteInteira);
			}
		}
		String strVal = StringUtil.getStringValueToInterface(value);
		int length = strVal.length();
		StringBuffer sb = new StringBuffer(strVal);
		for (int i = length - 1; i >= 0; i--) {
			if ('0' == sb.charAt(i)) {
				sb.setLength(i);
			} else {
				break;
			}
		}
		return sb.toString();
	}
	
	public static void resetImages() {
		NOIMAGE = null;
		LOADINGIMAGE = null;
	}

	public static void setListSort(GridListContainer listContainer, String[]... columns) {
		listContainer.setColsSort(columns);
	}
}
