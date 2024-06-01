package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.util.Date;

public class DapLaudo extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPDAPLAUDO";
	
	public static final String FLSTATUSLAUDO_ABERTO = "A";
	public static final String FLSTATUSLAUDO_FECHADO = "F";
	public static final String FLSTATUSLAUDO_EDITADO = "E";
	public static final String FLSTATUSLAUDO_EM_EDICAO = "D";

	public String cdEmpresa;
	public String cdCliente;
	public String cdDapMatricula;
	public String cdSafra;
	public String cdDapCultura;
	public String cdDapLaudo;
	public int nuSeqLaudo;
	public String flStatusLaudo;
	public double qtArea;
	public Date dtEmissao;
	public String dsAspectoCultura;
	public String dsRecomendacoes;
	public byte[] imAssTecnico;
	public byte[] imAssCliente;
	public double cdLatitude;
	public double cdLongitude;
	public String dsObservacao;
	
	//Não persistente
	public DapCultura dapCultura;
	public String nmRazaoSocial;
	public String nuCnpj;
	public String dsCidade;
	public String dsLocalidade;
	public String dsSafra;
	public String cdUf;
	public boolean enviadoServidor;

	public DapLaudo() {}

	public DapLaudo(DapLaudoAtua dapLaudoAtua) {
		this.cdEmpresa = dapLaudoAtua.cdEmpresa;
		this.cdCliente = dapLaudoAtua.cdCliente;
		this.cdDapMatricula = dapLaudoAtua.cdDapMatricula;
		this.cdSafra = dapLaudoAtua.cdSafra;
		this.cdDapCultura = dapLaudoAtua.cdDapCultura;
		this.cdDapLaudo = dapLaudoAtua.cdDapLaudo;
		this.nuSeqLaudo = dapLaudoAtua.nuSeqLaudo;
		this.flStatusLaudo = dapLaudoAtua.flStatusLaudo;
		this.qtArea = dapLaudoAtua.qtArea;
		this.dtEmissao = dapLaudoAtua.dtEmissao;
		this.dsAspectoCultura = dapLaudoAtua.dsAspectoCultura;
		this.dsRecomendacoes = dapLaudoAtua.dsRecomendacoes;
		this.cdLatitude = dapLaudoAtua.cdLatitude;
		this.cdLongitude = dapLaudoAtua.cdLongitude;
		this.dsObservacao = dapLaudoAtua.dsObservacao;
	}

	public String getPosGeografica() {
		if (ValueUtil.isNotEmpty(cdLatitude) && ValueUtil.isNotEmpty(cdLongitude)) {
			return StringUtil.getStringValue(cdLatitude, 5) + ", " + StringUtil.getStringValue(cdLongitude, 5);
		}
		return ValueUtil.VALOR_NI;
	}
	
	public static String getImagePath() {
		return FotoUtil.getPathImg(DapLaudo.class);
	}
	
	public String getDsLocal() {
		return (ValueUtil.isNotEmpty(dsCidade) ? dsCidade + " - " : "") + (ValueUtil.isNotEmpty(cdUf) ? cdUf + " - ": "") + (ValueUtil.isNotEmpty(dsLocalidade) ? dsLocalidade : "");
	}
	
	@Override
	public String getCdDomain() {
		return cdDapMatricula;
	}

	@Override
	public String getDsDomain() {
		return dsAspectoCultura;
	}

	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdDapMatricula);
        primaryKey.append(";");
        primaryKey.append(cdSafra);
        primaryKey.append(";");
        primaryKey.append(cdDapCultura);
        primaryKey.append(";");
        primaryKey.append(cdDapLaudo);
        return primaryKey.toString();
	}
	
	public boolean isDapFechado() {
		return ValueUtil.valueEquals(flStatusLaudo, FLSTATUSLAUDO_FECHADO);
	}
	
	public boolean isDapAberto() {
		return ValueUtil.valueEquals(flStatusLaudo, FLSTATUSLAUDO_ABERTO);
	}
	
	public boolean isDapEditado() {
		return ValueUtil.valueEquals(flStatusLaudo, FLSTATUSLAUDO_EDITADO);
	}
	
	public boolean isDapEmEdicao() {
		return ValueUtil.valueEquals(flStatusLaudo, FLSTATUSLAUDO_EM_EDICAO);
	}
	
	public String getDsFilePathPdfDapLaudo() {
		String filePath = Convert.appendPath(BaseDomain.getDirBaseArquivosInCard(), "PdfDapLaudo");
		if (VmUtil.isAndroid()) {
			return Convert.appendPath("/sdcard" , filePath);
		} else {
			return Convert.appendPath(Settings.appPath, filePath);
		}
	}

	public String getNomeArquivoPdf() {
		String dtEmissaoStr = DateUtil.formatDateDDMMYYYY(dtEmissao);
		return cdDapLaudo + "_" + dtEmissaoStr.replace("/", "-") + "_" + StringUtil.changeStringAccented(nmRazaoSocial).replaceAll("[\\ \\/]", "_"); 
	}
	
	public String getDsDapLaudo() {
		return dapCultura.dsDapCultura +" ["+ nuSeqLaudo+ "]";
	}

	public String getDsCidadeEstado() {
		return dsCidade + " - "+cdUf;
	}
	
	public String getDtEmissaoToString() {
		if (dtEmissao != null) {
			return dtEmissao.toString();
		}
		return ValueUtil.VALOR_NI;
	}
	
	public String getNmAssinaturaTec() {
		return "Ass_" + cdDapLaudo + "_Tecnico";
	}
	
	public String getNmAssinaturaCli() {
		return "Ass_" + cdDapLaudo + "_Cliente";
	}
	
	public String getNmImgAssinaturaTec() {
		if (!isDapEditado()) {
			return getNmAssinaturaTec() + FotoUtil.DSEXTESAO_JPG;
		}
		return null;
	}
	
	public String getNmImgAssinaturaCli() {
		if (!isDapEditado()) {
			return getNmAssinaturaCli() + FotoUtil.DSEXTESAO_JPG;
		}
		return null;
	}
	
	public boolean isClientePessoaFisica() {
		int count = 0;
		if (ValueUtil.isNotEmpty(nuCnpj)) {
			char[] chars = nuCnpj.toCharArray();
			int len = chars.length;
			for (int i = 0; i < len; i++) {
				char caracter = chars[i];
				if (ValueUtil.isValidNumberChar(caracter)) {
					count++;
				}
			}
		}
		return count == 11;
	}
	
	public String getDsStatusLaudo() {
		switch (flStatusLaudo) {
		case FLSTATUSLAUDO_ABERTO:
			return Messages.DAPLAUDO_DSSTATUSLAUDO_ABERTO;
		case FLSTATUSLAUDO_FECHADO:
			return Messages.DAPLAUDO_DSSTATUSLAUDO_FECHADO;
		case FLSTATUSLAUDO_EDITADO:
			return Messages.DAPLAUDO_DSSTATUSLAUDO_EDITADO;
		case FLSTATUSLAUDO_EM_EDICAO:
			return Messages.DAPLAUDO_DSSTATUSLAUDO_EM_EDICAO;
		default :
			return ValueUtil.VALOR_NI;
		}
	}
	
}
