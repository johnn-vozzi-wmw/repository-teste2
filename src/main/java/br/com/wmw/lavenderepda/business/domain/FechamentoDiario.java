package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import totalcross.util.Date;

public class FechamentoDiario extends BaseDomain {

	public static String TABLE_NAME = "TBLVPFECHAMENTODIARIO";

    public String cdEmpresa;
    public String cdRepresentante;
    public Date dtFechamentoDiario;
    public String hrFechamentoDiario;
    public double vlTotalDepositoBancario;
    public String hrAlteracao;
    public Date dtAlteracao;
    public Date dtFinalizacao;
    public String hrFinalizacao;
    public double vlTotalPedidos;
    public double vlTotalReceitas; 			
    public double vlTotalVendas;		
    public double vlTotalVendasDinheiro;
    public double vlTotalVendasCheque;	
    public double vlTotalVendasBoleto;		
    public double vlTotalPagamentos;	
    public double vlTotalPagamentosDinheiro; 
    public double vlTotalPagamentosCheque;
    public double vlTotalPagamentosBoleto;	
    public double vlTotalBonificacao;
    public double vlTotalCreditoCliente;
    public double vlTotalDesconto;
    public int nuImpressao;
    public String flLiberadoSenha;
    public String flAgrupado;
    public double vlTotalVendasOutros;
    public double vlTotalPagamentosOutros;
    public String dsPlacaVeiculo;
    public double kmInicialVeiculo;
    public double kmFinalVeiculo;
    
    //Não Persistente
    public Date dtFechamentoDiarioFilter;
    public boolean atualizandoNuImpressao;

	public FechamentoDiario() {

    }
    
    public FechamentoDiario(Date dtFechamentoDiario) {
    	this.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	this.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(FechamentoDiario.class);
    	this.dtFechamentoDiario = dtFechamentoDiario;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FechamentoDiario) {
            FechamentoDiario fechamentodiario = (FechamentoDiario) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, fechamentodiario.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, fechamentodiario.cdRepresentante) && 
                ValueUtil.valueEquals(dtFechamentoDiario, fechamentodiario.dtFechamentoDiario);
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
        primaryKey.append(dtFechamentoDiario);
        return primaryKey.toString();
    }
    
    public boolean isLiberadoPorSenha() {
    	return ValueUtil.getBooleanValue(flLiberadoSenha);
    }
    
    public boolean isAgrupado() {
    	return ValueUtil.getBooleanValue(flAgrupado);
    }

}