package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.builder.ProducaoProdBuilder;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.util.Date;

public class ProducaoProd extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPPRODUCAOPROD";

    public String cdEmpresa;
    public String cdProduto;
    public Date dtInicial;
    public Date dtFinal;
    public double qtdProducaoProd;
    public double qtdDisponivel;
    public String flEstoqueExcluido;
    //Nao persistente
    public double qtdReserva;
	public String dsProdutoFilter;
	public Date dtFilter;
	public String cdRepresentante;
	public Date dtFinalFilter;

	public ProducaoProd() {
	}
			
    public ProducaoProd(ProducaoProdBuilder producaoProdBuilder) {
    	this.cdEmpresa = producaoProdBuilder.cdEmpresa;
    	this.cdProduto = producaoProdBuilder.cdProduto;
    }

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof ProducaoProd) {
            ProducaoProd producaoProd = (ProducaoProd) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, producaoProd.cdEmpresa) && 
                ValueUtil.valueEquals(cdProduto, producaoProd.cdProduto) && 
                ValueUtil.valueEquals(dtInicial, producaoProd.dtInicial) && 
                ValueUtil.valueEquals(dtFinal, producaoProd.dtFinal);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(dtInicial);
        primaryKey.append(";");
        primaryKey.append(dtFinal);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdProduto;
	}

	@Override
	public String getDsDomain() {
		try {
			return ProdutoService.getInstance().getDsProduto(cdProduto);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		return ValueUtil.VALOR_NI;
	}

}