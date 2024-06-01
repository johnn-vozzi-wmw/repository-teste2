package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoBoletoDTO;
import totalcross.util.BigDecimal;
import totalcross.util.Date;

public class PedidoBoleto extends BaseDomain {
	
	public static final String NMCOLUNA_NUSEQUENCIABOLETOPEDIDO = "NUSEQUENCIABOLETOPEDIDO";
	public static final String DIGITOVEFICADOR_P = "P";
	public static final String TEXTO_RESP_CDEDENTE_1 = "1";
	public static final String TEXTO_RESP_CDEDENTE_2 = "2";
	public static final String TEXTO_RESP_CDEDENTE_3 = "3";
	public static final String TEXTO_RESP_CDEDENTE_4 = "4";
	public static final String TEXTO_RESP_CDEDENTE_5 = "5";
	
	public static final String TABLE_NAME = "TBLVPPEDIDOBOLETO";
	
	public static String CDPAGAMENTOPEDIDO_ZERO = "0";
	public static String CD_MOEDA_REAL = "9";
	public static String DSLOCALPAGAMENTO = "PAGÁVEL EM QUALQUER BANCO ATÉ O VENCIMENTO";
	public static String DSESPECIE = "REAL";
	public static String FLACEITE = "A";
	public static String DSESPECIEDOCUMENTO = "DM";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public int nuSequenciaBoletoPedido;
	public String cdPagamentoPedido;
    public String cdBarras;
    public String dsLinhasDigitavel;
    public Date dtVencimento;
    public String nuAgenciaCodigoCedente;
    public Date dtDocumento;
    public BigDecimal nuDocumento;
    public double vlBoleto;
    public String nuNossoNumero;
    public String dsLocalPagamento;
    public String nuCarteira;
    public String dsEspecieDocumento;
    public String dsObsCedente;
    public String flAceite;
    public String dsEspecie;
    public Date dtProcessamento;
    public String cdBeneficiario;
    public String cdBoletoConfig;
    public String nuTitulo;
    public String nuSubDoc;
    
    //Não persisti
    public String cdCliente;
    public String nmCliente;
    public String[] cdRepresentanteSupList;
    
    public PedidoBoleto() {
	}
    
    public PedidoBoleto(PedidoBoletoDTO pedidoBoletoDTO) {
    	this();
    	try {
    		FieldMapper.copy(pedidoBoletoDTO, this);
    	} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PedidoBoleto) {
            PedidoBoleto pedidoBoleto = (PedidoBoleto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pedidoBoleto.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, pedidoBoleto.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, pedidoBoleto.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, pedidoBoleto.nuPedido) && 
                ValueUtil.valueEquals(nuSequenciaBoletoPedido, pedidoBoleto.nuSequenciaBoletoPedido) &&
                ValueUtil.valueEquals(cdPagamentoPedido, pedidoBoleto.cdPagamentoPedido);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuSequenciaBoletoPedido);
        primaryKey.append(";");
        primaryKey.append(cdPagamentoPedido);
        return primaryKey.toString();
    }

}