package br.com.wmw.lavenderepda.business.domain;

import java.lang.reflect.Field;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PedidoLog extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPPEDIDOLOG";

	public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public Date dtAcao;
    public String hrAcao;
    public String cdMotivoCancelamento;
    public String cdStatusPedido;
    public double vlTotalPedido;
    public Date dtEntrega;
    public Date dtEntregaLiberada;
    public String cdUsuarioLibEntrega;
    public String cdTipoPedido;
    public String nuOrdemCompraCliente;
    public String cdCondicaoComercial;
    public String dsObservacao;
    public String dsObservacaoNotaFiscal;
    public String flEmergencial;
    public String cdMotivoEmergencia;
    public String dsObsEmergencia;
    public String nuPedidoRelEmergencial;
    public String cdUsuarioAutorizacao;
    public String flEnviaEmail;
    public String dsEmailsDestino;
    public String cdEnderecoCliente;
    public String cdTipoPagamento;
    public String flTipoRegistro;
    public String flOrigemAcao;
    public String cdUsuarioCriacao;
    
    //Não persistentes
	public boolean isAlterado;
	public boolean onlyEnviadosServidor;
    
	public PedidoLog() {
	}

	public PedidoLog(Builder builder) {
    	cdEmpresa = builder.cdEmpresa;
    	cdRepresentante = builder.cdRepresentante;
    	flOrigemPedido = builder.flOrigemPedido;
    	nuPedido = builder.nuPedido;
    	dtAcao = DateUtil.getCurrentDate();
    	hrAcao = TimeUtil.getCurrentTimeHHMMSS();
    	cdMotivoCancelamento = builder.cdMotivoCancelamento;
    	cdStatusPedido = builder.cdStatusPedido;
    	vlTotalPedido = builder.vlTotalPedido;
    	dtEntrega = builder.dtEntrega;
    	dtEntregaLiberada = builder.dtEntregaLiberada;
    	cdUsuarioLibEntrega = builder.cdUsuarioLibEntrega;
    	cdTipoPedido = builder.cdTipoPedido;
    	nuOrdemCompraCliente = builder.nuOrdemCompraCliente;
    	cdCondicaoComercial = builder.cdCondicaoComercial;
    	dsObservacao = builder.dsObservacao;
    	dsObservacaoNotaFiscal = builder.dsObservacaoNotaFiscal;
    	flEmergencial = builder.flEmergencial;
    	cdMotivoEmergencia = builder.cdMotivoEmergencia;
    	dsObsEmergencia = builder.dsObsEmergencia;
    	nuPedidoRelEmergencial = builder.nuPedidoRelEmergencial;
    	cdUsuarioAutorizacao = builder.cdUsuarioAutorizacao;
    	flEnviaEmail = builder.flEnviaEmail;
    	dsEmailsDestino = builder.dsEmailsDestino;
    	cdEnderecoCliente = builder.cdEnderecoCliente;
    	cdTipoPagamento = builder.cdTipoPagamento;
    	flTipoRegistro = builder.flTipoRegistro;
    	flOrigemAcao = builder.flOrigemAcao;
    	flTipoAlteracao = builder.flTipoAlteracao;
	}
	
	//Override
    public boolean equals(Object obj) {
        if (obj instanceof PedidoLog) {
            PedidoLog pedidoLog = (PedidoLog) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pedidoLog.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, pedidoLog.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, pedidoLog.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, pedidoLog.nuPedido) &&
                ValueUtil.valueEquals(dtAcao, pedidoLog.dtAcao) &&
                ValueUtil.valueEquals(hrAcao, pedidoLog.hrAcao);
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
        primaryKey.append(dtAcao);
        primaryKey.append(";");
        primaryKey.append(hrAcao);
        return primaryKey.toString();
    }

	public static class Builder {

		public String cdEmpresa;
	    public String cdRepresentante;
	    public String flOrigemPedido;
	    public String nuPedido;
	    public Date dtAcao;
	    public String hrAcao;
	    
	    public String cdMotivoCancelamento;
	    public String cdStatusPedido;
	    public double vlTotalPedido;
	    public Date dtEntrega;
	    public Date dtEntregaLiberada;
	    public String cdUsuarioLibEntrega;
	    public String cdTipoPedido;
	    public String nuOrdemCompraCliente;
	    public String cdCondicaoComercial;
	    public String dsObservacao;
	    public String dsObservacaoNotaFiscal;
	    public String flEmergencial;
	    public String cdMotivoEmergencia;
	    public String dsObsEmergencia;
	    public String nuPedidoRelEmergencial;
	    public String cdUsuarioAutorizacao;
	    public String flEnviaEmail;
	    public String dsEmailsDestino;
	    public String cdEnderecoCliente;
	    public String cdTipoPagamento;
	    public String flTipoRegistro;
	    public String flTipoAlteracao;
	    public String flOrigemAcao;
	    
		public Builder(final Pedido pedido) {
			this.cdEmpresa = pedido.cdEmpresa;
			this.cdRepresentante = pedido.cdRepresentante;
			this.flOrigemPedido = pedido.flOrigemPedido;
			this.nuPedido = pedido.nuPedido;
			this.dtAcao = DateUtil.getCurrentDate();
			this.hrAcao = TimeUtil.getCurrentTimeHHMMSS();
			this.flOrigemAcao = OrigemPedido.FLORIGEMPEDIDO_PDA;
		}

		public Builder completo(final Pedido pedido) {
			this.cdMotivoCancelamento = pedido.cdMotivoCancelamento;
			this.cdStatusPedido = pedido.cdStatusPedido;
			this.vlTotalPedido = pedido.vlTotalPedido;
			this.dtEntrega = pedido.dtEntrega;
			this.dtEntregaLiberada = pedido.dtEntregaLiberada;
			this.cdUsuarioLibEntrega = pedido.cdUsuarioLibEntrega;
			this.cdTipoPedido = pedido.cdTipoPedido;
			this.nuOrdemCompraCliente = pedido.nuOrdemCompraCliente;
			this.cdCondicaoComercial = pedido.cdCondicaoComercial;
			this.dsObservacao = pedido.getHashValuesDinamicos().getString(Pedido.NMCOLUNA_DSOBSERVACAO);
			this.flEnviaEmail = pedido.getHashValuesDinamicos().getString(Pedido.NMCOLUNA_FLENVIAEMAIL);
			this.dsEmailsDestino = pedido.getHashValuesDinamicos().getString(Pedido.NMCOLUNA_DSEMAILSDESTINO);
			this.cdEnderecoCliente = pedido.cdEnderecoCliente;
			this.cdTipoPagamento = pedido.cdTipoPagamento;
			this.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
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
		
		public Builder comCdMotivoCancelamento(String newCdMotivoCancelamento) {
			this.cdMotivoCancelamento = newCdMotivoCancelamento;
			return this;
		}

		public Builder comCdStatusPedido(String newCdStatusPedido) {
			this.cdStatusPedido = newCdStatusPedido;
			return this;
		}

		public Builder comVlTotalPedido(double newVlTotalPedido) {
			this.vlTotalPedido = newVlTotalPedido;
			return this;
		}

		public Builder comDtEntrega(Date newDtEntrega) {
			this.dtEntrega = newDtEntrega;
			return this;
		}

		public Builder comDtEntregaLiberada(Date newDtEntregaLiberada) {
			this.dtEntregaLiberada = newDtEntregaLiberada;
			return this;
		}

		public Builder comCdTipoPedido(String newCdTipoPedido) {
			this.cdTipoPedido = newCdTipoPedido;
			return this;
		}

		public Builder comNuOrdemCompraCliente(String newNuOrdemCompraCliente) {
			this.nuOrdemCompraCliente = newNuOrdemCompraCliente;
			return this;
		}

		public Builder comCdCondicaoComercial(String newCdCondicaoComercial) {
			this.cdCondicaoComercial = newCdCondicaoComercial;
			return this;
		}

		public Builder comDsObservacao(String newDsObservacao) {
			this.dsObservacao = newDsObservacao;
			return this;
		}

		public Builder comDsObservacaoNotaFiscal(String newDsObservacaoNotaFiscal) {
			this.dsObservacaoNotaFiscal = newDsObservacaoNotaFiscal;
			return this;
		}

		public Builder comFlEmergencial(String newFlEmergencial) {
			this.flEmergencial = newFlEmergencial;
			return this;
		}

		public Builder comCdMotivoEmergencia(String newCdMotivoEmergencia) {
			this.cdMotivoEmergencia = newCdMotivoEmergencia;
			return this;
		}

		public Builder comDsObsEmergencia(String newDsObsEmergencia) {
			this.dsObsEmergencia = newDsObsEmergencia;
			return this;
		}

		public Builder comNuPedidoRelEmergencial(String newNuPedidoRelEmergencial) {
			this.nuPedidoRelEmergencial = newNuPedidoRelEmergencial;
			return this;
		}

		public Builder comCdUsuarioAutorizacao(String newCdUsuarioAutorizacao) {
			this.cdUsuarioAutorizacao = newCdUsuarioAutorizacao;
			return this;
		}

		public Builder comFlEnviaEmail(String newFlEnviaEmail) {
			this.flEnviaEmail = newFlEnviaEmail;
			return this;
		}

		public Builder comDsEmailsDestino(String newDsEmailsDestino) {
			this.dsEmailsDestino = newDsEmailsDestino;
			return this;
		}

		public Builder comCdEnderecoCliente(String newCdEnderecoCliente) {
			this.cdEnderecoCliente = newCdEnderecoCliente;
			return this;
		}
		
		public Builder comCdTipoPagamento(String newCdTipoPagamento) {
			this.cdTipoPagamento = newCdTipoPagamento;
			return this;
		}

		public PedidoLog build() {
			return new PedidoLog(this);
		}
	}
	
	public static BaseDomain comparableChangedProperties(final BaseDomain newDomain, final BaseDomain oldDomain, final PedidoLog domainDestination, final String tipoRegistro) {
		if (newDomain != null && oldDomain != null) {
			Field[] fields = PedidoLog.class.getDeclaredFields();
			if (ValueUtil.isNotEmpty(fields)) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					try {
						Object valorOriginal = field.get(oldDomain);
						Object valorAtual = field.get(newDomain);
						if (field.getType().equals(Date.class)) {
							if (valorOriginal == null) {
								valorOriginal = DateUtil.getNullDate();
							}
							if (valorAtual == null) {
								valorAtual = DateUtil.getNullDate();
							}
						} else if (field.getType().equals(String.class)) {
							if (ValueUtil.isEmpty((String)valorOriginal) && ValueUtil.isEmpty((String)valorAtual)) {
								continue;
							}
						}
						if (field.getType().equals(String.class)) {
							if (valorOriginal == null) {
								valorOriginal = ValueUtil.VALOR_NI;
							}
							if (valorAtual == null) {
								valorAtual = ValueUtil.VALOR_NI;
							}
						}
						if (!ValueUtil.valueEquals(valorAtual, valorOriginal) ) {
							try {
								if (!"hrAcao".equals(field.getName()) && !"dtAcao".equals(field.getName()) && !"flTipoRegistro".equals(field.getName()) && !"cdUsuarioCriacao".equals(field.getName())) {
									switch (tipoRegistro) {
										case TipoRegistro.ALTERACAO:
											if (isPedidoAlteracao(tipoRegistro, field)) {
												registrarAlteracaoDomain(domainDestination, field, valorAtual);
											}
											break;
										case TipoRegistro.INCLUSAO:
											registrarAlteracaoDomain(domainDestination, field, valorAtual);
											break;
										case TipoRegistro.FECHAMENTO:
											if (isPedidoFechamento(tipoRegistro, field)) {
												registrarAlteracaoDomain(domainDestination, field, valorAtual);
											}
											break;
										case TipoRegistro.CANCELAMENTO:
											if (isPedidoCancelamento(tipoRegistro, field)) {
												registrarAlteracaoDomain(domainDestination, field, valorAtual);
											}
											break;
										case TipoRegistro.REABERTURA:
											if (isPedidoReabertura(tipoRegistro, field)) {
												registrarAlteracaoDomain(domainDestination, field, valorAtual);
											}
											break;
										default:
											break;
									}
								}
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
	
	private static boolean isPedidoFechamento(final String tipoRegistro, Field field) {
		return "vlTotalPedido".equals(field.getName()) || "cdStatusPedido".equals(field.getName());
	}

	private static boolean isPedidoCancelamento(final String tipoRegistro, Field field) {
		return "cdMotivoCancelamento".equals(field.getName()) || "cdStatusPedido".equals(field.getName());
	}

	private static boolean isPedidoReabertura(final String tipoRegistro, Field field) {
		return "cdStatusPedido".equals(field.getName());
	}

	private static boolean isPedidoAlteracao(final String tipoRegistro, Field field) {
		return 	!"vlTotalPedido".equals(field.getName()) && !"cdStatusPedido".equals(field.getName()) && !"cdMotivoCancelamento".equals(field.getName());
	}

	private static void registrarAlteracaoDomain(final PedidoLog domainDestination, Field field, Object valorAtual) throws IllegalAccessException {
		field.set(domainDestination, valorAtual);
		domainDestination.isAlterado = true;
	}

}