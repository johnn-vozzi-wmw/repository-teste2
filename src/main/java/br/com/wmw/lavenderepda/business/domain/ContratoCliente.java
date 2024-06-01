package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ContratoCliente extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONTRATOCLIENTE";

	public static final String FORECAST = "C";
	public static final String GRADE = "G";

	public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String flTipoContrato;
    public Date dtVigenciaInicial;
    public Date dtVigenciaFinal;
    public int nuDiasPeriodicidade;
    public String cdTabelaPreco;
    public String cdCondicaoPagamento;

    //Não persistentes
    public Date dtVigenciaFilter;
    public Vector contratoProdutoList;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ContratoCliente) {
            ContratoCliente contratoCliente = (ContratoCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, contratoCliente.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, contratoCliente.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, contratoCliente.cdCliente) &&
                ValueUtil.valueEquals(flTipoContrato, contratoCliente.flTipoContrato) &&
                ValueUtil.valueEquals(dtVigenciaInicial, contratoCliente.dtVigenciaInicial);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(flTipoContrato);
        primaryKey.append(";");
        primaryKey.append(dtVigenciaInicial);
        return primaryKey.toString();
    }

}