package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import totalcross.sys.Settings;
import totalcross.util.Date;

public class TituloFinanceiro extends BasePersonDomain {

    public static String TABLE_NAME = "TBLVPTITULOFINANCEIRO";

	public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String nuNf;
    public String nuSerie;
    public String nuTitulo;
    public String nuSubDoc;
    public double vlNf;
    public double vlTitulo;
    public double vlPago;
    public Date dtEmissao;
    public Date dtVencimento;
    public String dsHistorico;
    public String dsObservacao;
    public String cdTipoPagamento;
    public Date dtPagamento;
    //--
    public Date dtVencimentoFilter;
    public boolean filtraSomenteNaoPagos;
    public boolean ordenacaoDinamica;
    public static String sortAttr;
    public Date dtIncialPagamento;
    public Date dtFinalPagamento;
    public Date dtIncialVencimento;
    public Date dtFinalVencimento;
    public String statusTituloFinanceiro;
    public String cdRedeFilter;
    
    public Cliente cliente;

    public TituloFinanceiro() {
		super(null);
	}

    public TituloFinanceiro(String tableName) {
		super(tableName);
	}


    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TituloFinanceiro) {
            TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tituloFinanceiro.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tituloFinanceiro.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, tituloFinanceiro.cdCliente) &&
                ValueUtil.valueEquals(nuNf, tituloFinanceiro.nuNf) &&
                ValueUtil.valueEquals(nuSerie, tituloFinanceiro.nuSerie) &&
                ValueUtil.valueEquals(nuTitulo, tituloFinanceiro.nuTitulo) &&
                ValueUtil.valueEquals(nuSubDoc, tituloFinanceiro.nuSubDoc);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
    	strBuffer.append(";");
    	strBuffer.append(nuNf);
    	strBuffer.append(";");
    	strBuffer.append(nuSerie);
    	strBuffer.append(";");
    	strBuffer.append(nuTitulo);
    	strBuffer.append(";");
    	strBuffer.append(nuSubDoc);
        return strBuffer.toString();
    }
    
	public String getDsFilePathPdfNfPedido() {
		String filePath = BaseDomain.getDirBaseArquivosInCard() + "PdfNfPedido/";
		if (VmUtil.isAndroid()) {
			return "/sdcard/" + filePath;
		} else {
			return Settings.appPath + "/" + filePath;
		}
	}

	public String getDsFilePathPdfTituloBoletoPedido() {
		String filePath = BaseDomain.getDirBaseArquivosInCard() + "PdfTituloBoletoPedido/";
		if (VmUtil.isAndroid()) {
			return "/sdcard/" + filePath;
		} else {
			return Settings.appPath + "/" + filePath;
		}
	}
    
    public String getNomeArquivoPdf() throws SQLException {
		String dtEmissaoStr = null;
		if (dtEmissao == null) {
			dtEmissaoStr = DateUtil.formatDateDDMMYYYY(DateUtil.getCurrentDate());
		} else {
			dtEmissaoStr = DateUtil.formatDateDDMMYYYY(dtEmissao);
		}
		return nuTitulo + "_" + dtEmissaoStr.replace("/", "-") + "_" + StringUtil.changeStringAccented(getCliente().nmRazaoSocial).replaceAll("[\\ \\/]", "_") ; 
	}
    
	public Cliente getCliente() throws SQLException {
		if (!ValueUtil.isEmpty(cdCliente) && ((cliente == null) || (!ValueUtil.valueEquals(cdCliente, cliente.cdCliente)))) {
			cliente = ClienteService.getInstance().getCliente(cdEmpresa, cdRepresentante, cdCliente);
		}
		return cliente;
	}

}