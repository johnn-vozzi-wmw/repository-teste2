package br.com.wmw.lavenderepda.business.domain;

import java.lang.reflect.Field;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ItemPedidoLog extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPITEMPEDIDOLOG";

	public String cdEmpresa;
	public String flOrigemPedido;
	public String cdRepresentante;
	public String nuPedido;
	public String cdProduto;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public Date dtAcao;
	public String hrAcao;
	public String cdUnidade;
	public double qtItemFisico;
	public double qtItemFaturamento;
	public double vlItemPedido;
	public double vlTotalItemPedido;
	public double vlPctDesconto;
	public double vlPctAcrescimo;
	public double vlVerbaItem;
	public double vlVerbaItemPositivo;
	public String cdUsuarioLiberacao;
	public String flTipoRegistro;
	public String flOrigemAcao;
	public String cdUsuarioCriacao;
	
	//Não persistentes
	public static boolean isAlterado;
	public boolean onlyEnviadosServidorFilter;
	
	public ItemPedidoLog() {
		//--
	}
    
	public ItemPedidoLog(Builder builder) {
    	cdEmpresa = builder.cdEmpresa;
    	cdRepresentante = builder.cdRepresentante;
    	flOrigemPedido = builder.flOrigemPedido;
    	nuPedido = builder.nuPedido;
    	cdProduto = builder.cdProduto;
    	flTipoItemPedido = builder.flTipoItemPedido;
    	nuSeqProduto = builder.nuSeqProduto;
    	dtAcao = DateUtil.getCurrentDate();
    	hrAcao = TimeUtil.getCurrentTimeHHMMSS();
    	cdEmpresa = builder.cdEmpresa;
    	flOrigemPedido = builder.flOrigemPedido;
    	cdRepresentante = builder.cdRepresentante;
    	nuPedido = builder.nuPedido;
    	cdUnidade = builder.cdUnidade;
    	qtItemFisico = builder.qtItemFisico;
    	qtItemFaturamento = builder.qtItemFaturamento;
    	vlItemPedido = builder.vlItemPedido;
    	vlTotalItemPedido = builder.vlTotalItemPedido;
    	vlPctDesconto = builder.vlPctDesconto;
    	vlPctAcrescimo = builder.vlPctAcrescimo;
    	vlVerbaItem = builder.vlVerbaItem;
    	vlVerbaItemPositivo = builder.vlVerbaItemPositivo;
    	cdUsuarioLiberacao = builder.cdUsuarioLiberacao;
    	flTipoRegistro = builder.flTipoRegistro;
    	flOrigemAcao = builder.flOrigemAcao;
    	flTipoAlteracao = builder.flTipoAlteracao;
	}
	
	//Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemPedidoLog) {
        	ItemPedidoLog itemPedidoLog = (ItemPedidoLog) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemPedidoLog.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, itemPedidoLog.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, itemPedidoLog.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, itemPedidoLog.nuPedido) &&
                ValueUtil.valueEquals(cdProduto, itemPedidoLog.cdProduto) &&
                ValueUtil.valueEquals(flTipoItemPedido, itemPedidoLog.flTipoItemPedido) &&
                (nuSeqProduto == itemPedidoLog.nuSeqProduto) &&
                ValueUtil.valueEquals(dtAcao, itemPedidoLog.dtAcao) &&
                ValueUtil.valueEquals(hrAcao, itemPedidoLog.hrAcao);
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
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(flTipoItemPedido);
        primaryKey.append(";");
        primaryKey.append(nuSeqProduto);
        primaryKey.append(";");
        primaryKey.append(dtAcao);
        primaryKey.append(";");
        primaryKey.append(hrAcao);
        return primaryKey.toString();
    }

	public static class Builder {

		public String cdEmpresa;
		public String flOrigemPedido;
		public String cdRepresentante;
		public String nuPedido;
		public String cdProduto;
		public String flTipoItemPedido;
		public int nuSeqProduto;
		public Date dtAcao;
		public String hrAcao;

		public String cdUnidade;
		public double qtItemFisico;
		public double qtItemFaturamento;
		public double vlItemPedido;
		public double vlTotalItemPedido;
		public double vlPctDesconto;
		public double vlPctAcrescimo;
		public double vlVerbaItem;
		public double vlVerbaItemPositivo;
		public String cdUsuarioLiberacao;
			   
		public String flTipoRegistro;
		public String flTipoAlteracao;
		public String flOrigemAcao;

		public Builder(final ItemPedido itemPedido) {
			this.cdEmpresa = itemPedido.cdEmpresa;
			this.cdRepresentante = itemPedido.cdRepresentante;
			this.flOrigemPedido = itemPedido.flOrigemPedido;
			this.nuPedido = itemPedido.nuPedido;
			this.cdProduto = itemPedido.cdProduto;
			this.flTipoItemPedido = itemPedido.flTipoItemPedido;
			this.nuSeqProduto = itemPedido.nuSeqProduto;
			this.cdUnidade = itemPedido.cdUnidade;
			this.vlItemPedido = itemPedido.vlItemPedido;
		}

		public Builder completo(final ItemPedido itemPedido) {
			this.cdEmpresa = itemPedido.cdEmpresa;
			this.flOrigemPedido = itemPedido.flOrigemPedido;
			this.cdRepresentante = itemPedido.cdRepresentante;
			this.nuPedido = itemPedido.nuPedido;
			this.dtAcao = DateUtil.getCurrentDate();
			this.hrAcao = TimeUtil.getCurrentTimeHHMMSS();
	
			this.cdUnidade = itemPedido.cdUnidade;
			this.qtItemFisico = itemPedido.getQtItemFisico();
			this.qtItemFaturamento = itemPedido.qtItemFaturamento;
			this.vlItemPedido = itemPedido.vlItemPedido;
			this.vlTotalItemPedido = itemPedido.vlTotalItemPedido;
			this.vlPctDesconto = itemPedido.vlPctDesconto;
			this.vlPctAcrescimo = itemPedido.vlPctAcrescimo;
			this.vlVerbaItem = itemPedido.vlVerbaItem;
			this.vlVerbaItemPositivo = itemPedido.vlVerbaItemPositivo;
			this.cdUsuarioLiberacao = itemPedido.cdUsuarioLiberacao;

			this.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
			this.flOrigemAcao = OrigemPedido.FLORIGEMPEDIDO_PDA;
			return this;
		}
    	
		public Builder flTipoRegistro(String newFlTipoRegistro) {
			this.flTipoRegistro = newFlTipoRegistro;
			return this;
		}
		
		public Builder flTipoAlteracao(String newFlTipoAlteracao) {
			this.flTipoAlteracao = newFlTipoAlteracao;
			return this;
		}
		
		public ItemPedidoLog build() {
			return new ItemPedidoLog(this);
		}
	}
	
	public static BaseDomain comparableChangedProperties(final BaseDomain newDomain, final BaseDomain oldDomain, final BaseDomain domainDestination) {
		if (newDomain != null && oldDomain != null) {
			Field[] fields = ItemPedidoLog.class.getDeclaredFields();
			if (ValueUtil.isNotEmpty(fields)) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					try {
						Object valorOriginal = field.get(oldDomain);
						Object valorAtual = field.get(newDomain);
						if (!ValueUtil.valueEquals(valorAtual, valorOriginal)) {
							try {
								field.set(domainDestination, valorAtual);
								isAlterado = !"hrAcao".equals(field.getName()) && !"dtAcao".equals(field.getName()) && !"flTipoRegistro".equals(field.getName()) && !"cdUsuarioCriacao".equals(field.getName());
							} catch (Throwable e) {
								// Não faz nada
							}
						}
					} catch (Throwable e) {
						// Não faz nada
					}
				}
			}
		}
		return domainDestination;
	}

}