package br.com.wmw.lavenderepda.util;

import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.io.File;
import totalcross.sys.Time;

public class MediaUtil {
	
	public enum MEDIA_ENTIDADE {
		CATALOGOEXTERNO,
		DIVULGAINFO,
		FOTOCLIENTEERP,
		FOTOPRODUTO,
		FOTOPRODUTOEMP,
		FOTOPRODUTOGRADE,
		VIDEOPRODUTO,
		VIDEOPRODUTOGRADE,
		MENUCATALOGO
	}
	
	public static boolean isArquivoFisicoAtualizado(FileProperties file) {
		try (File arguivo = new File(file.getAbsolutePath(), File.READ_ONLY)) {
			Time fileModifiedTime = arguivo.getTime(File.TIME_MODIFIED);
			String hrModificacao = getHrModificacao(file.getHrModificacao());
			Time memoryModifiedTime = getDtModificacao(file.getDtModificacao() + " " + hrModificacao);
			if (memoryModifiedTime == null) {
				return arguivo.exists();
			}
			return fileModifiedTime != null && fileModifiedTime.getTimeLong() > memoryModifiedTime.getTimeLong();
		} catch (Exception e) {
			
		}
		return false;
	}

	private static String getHrModificacao(String hr) {
		String hrModificacao = hr;
		if (ValueUtil.isEmpty(hrModificacao)) {
			hrModificacao = "00:00:00";
		}
		return hrModificacao;
	}
	
	private static Time getDtModificacao(String valueDtHr) {
		try {
			return TimeUtil.toTime(valueDtHr);
		} catch (Exception e) {
			return null;
		}
	}

}
