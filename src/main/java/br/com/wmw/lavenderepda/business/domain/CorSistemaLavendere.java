package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.CorSistema;

public class CorSistemaLavendere extends CorSistema {

    public static String TABLE_NAME = "TBLVPCORSISTEMA";

	public static final int PRODUTO_NA_PROMOCAO_FUNDO = 101;
	public static final int CLIENTE_ATRASADO_OU_BLOQUEADO_FUNDO = 103;
	public static final int CLIENTE_BLOQUEADO_FUNDO = 104;
	public static final int PRODUTO_SEM_ESTOQUE_FUNDO = 105;
	public static final int PRODUTO_INSERIDO_PEDIDO_FUNDO = 107;
	public static final int PRODUTO_DE_KIT_FUNDO = 109;
	public static final int PRODUTO_BONIFICACAO_FUNDO = 111;
	public static final int PRODUTO_AVISO_PRE_ALTA_FUNDO = 112;
	public static final int PRODUTO_VERBA_APOS_APLICAR_DESC_PROG_FUNDO = 113;
	public static final int CLIENTE_NU_DIAS_SEM_PEDIDO_EXTRAPOLADO_FUNDO = 115;
	public static final int PRODUTO_ESTOQUE_MINIMO = 117;
	public static final int ITEMPEDIDO_VERBA_MANUAL = 118;
	public static final int ITEMPEDIDO_RENTABILIDADE_BAIXA = 119;
	public static final int ITEMPEDIDO_RENTABILIDADE_ALTA = 120;
	public static final int COR_ITEMPEDIDO_COM_QTMAXVENDA_BACK = 121;
	public static final int COR_PEDIDOS_RECENTES_ABERTOSFECHADOS = 122;
	public static final int COR_PEDIDOS_RECENTES_NAOABERTOSEFECHADOS = 123;
	public static final int COR_PRODUTO_ULTIMO_PRECO_PRATICADO_CLIENTE = 126;
	public static final int COR_PRODUTO_DESC_PROMOCIONAL_BACK = 127;
	public static final int COR_PRODUTO_EM_OPORTUNIDADE = 128;
	public static final int COR_CLIENTE_JA_ATENDIDO = 129;
	public static final int COR_PRODUTO_COM_PRECO_EM_QUEDA = 130;
	public static final int COR_CARGA_PEDIDO_ABAIXO_PESO_MIN = 131;
	public static final int COR_CARGA_PEDIDO_ACIMA_PESO_MIN = 132;
	public static final int ITEMPEDIDO_RENTABILIDADE_DENTRO_TOLERANCIA = 133;
	public static final int CLIENTE_VISITA_ANDAMENTO = 134;
	public static final int CLIENTE_PROSPECT = 135;
	public static final int COR_FONTE_QUE_CONTEM_LOTE_PERCENTUAL_DE_VIDA_CRITICO = 136;
	public static final int COR_ITEMPEDIDO_PROBLEMA_RESERVA_ESTOQUE = 137;
	public static final int COR_CLIENTE_STATUS_BLOQUEIO_POR_ATRASO_FUNDO = 138;
	public static final int COR_CLIENTE_ATRASADO_GRID_FUNDO = 139;
	public static final int COR_PEDIDOS_DISP_LIBERACAO_GRID_FUNDO = 140;
	public static final int COR_PRODUTO_VENDIDO_MES_CORRENTE = 142;
	public static final int COR_ITEMPEDIDO_PENDENTE_GRID_FUNDO = 141;
	public static final int COR_PEDIDO_PENDENTE_ITENS_PENDENTES = 143;
	public static final int PRODUTO_SEM_ESTOQUE_GRADE_FUNDO = 144;
	public static final int COR_PEDIDO_CONSIGNACAO_DEVOLVIDO = 145;
	public static final int COR_PRODUTO_RESTRITO = 146;
	public static final int COR_PRODUTO_PENDENTE_RETIRADA = 147;
	public static final int COR_CLIENTE_PRODUTO_PENDENTE_RETIRADA_NAO_EXTRAPOLADO = 148;
	public static final int COR_CLIENTE_PRODUTO_PENDENTE_RETIRADA_EXTRAPOLADO = 149;
	public static final int COR_TRANSPORTADORA_TIPOFRETE_CIF = 150;
	public static final int COR_TRANSPORTADORA_TIPOFRETE_CIF_CONTRATO = 151;
	public static final int COR_FAIXA_DESCONTO_VOLUME_VENDA_MENSAL = 152;
	public static final int COR_FONTE_PRODUTO_LOTE_VIDA_UTIL_CRITICA = 153;
	public static final int COR_FUNDO_ERRO_CAD_CIDADEUF = 154;
	public static final int COR_LABEL_ERRO_CAD_CIDADEUF = 155;
	public static final int COR_FUNDO_CNPJ_DUPLICADO = 156; 
	public static final int COR_LABEL_CNPJ_DUPLICADO = 157;
	public static final int COR_FUNDO_META_NAO_ATINGIDA = 158;
	public static final int COR_FONTE_PRODUTO_JA_RATEADO = 159;
	public static final int COR_FUNDO_PRODUTO_INSERIDO_SUGVENDA = 160;
	public static final int COR_FUNDO_LINHA_LISTA_PEDIDOS_DIFERENCA = 161;
	public static final int COR_FUNDO_AGENDA_POSITIVADO = 162;
	public static final int COR_FUNDO_AGENDA_NAO_POSITIVADO = 163;
	public static final int COR_FUNDO_ITEM_DESC_MAX_ULTRAPASSADO = 165;
	public static final int COR_FUNDO_ITEM_ERRO_RECALCULO_VALORES = 166;
	public static final int COR_FUNDO_LISTA_FAIXA_DESC_QUANTIDADE_ATINGIDO = 167;
	public static final int	COR_FUNDO_CELULA_GRADE_COM_QUANTIDADE = 168;
	public static final int	COR_FUNDO_LISTA_CLIENTE_ATRASO = 169;
	public static final int	COR_FUNDO_LISTA_CLIENTE_SEM_LIMITE_CREDITO = 170;
	public static final int	COR_FUNDO_LISTA_PRODUTOS_FACEAMENTO = 171;
	public static final int	COR_FUNDO_CELULA_GRADE_SEM_PRECO = 173;
	public static final int COR_FUNDO_GRID_LOTE_PRODUTO_VINCULADO_TABELA_PRECO = 174;
	public static final int COR_GRID_PRODUTO_SUGESTAO_VENDA = 175;
	public static final int COR_CAPA_VALOR_PONTUACAO_POSITIVO = 176;
	public static final int COR_CAPA_VALOR_PONTUACAO_NEGATIVO = 177;
	public static final int COR_EXTRATO_VALOR_PONTUACAO_POSITIVO = 178;
	public static final int COR_EXTRATO_VALOR_PONTUACAO_NEGATIVO = 179;
	public static final int COR_FUNDO_LISTA_ITEM_PEDIDO_PROMOCIONAL = 180;
	public static final int COR_FUNDO_ITEM_SEM_QT_ITEM_FISICO_GONDOLA = 181;

	public static final int COR_PESQUISA_MERCADO_PRODUTO_VALOR_PREENCHIDO = 182;
	public static final int COR_FUNDO_LISTA_PLANEJAR_METAS_PLANEJADO = 183;
	public static final int COR_FUNDO_LISTA_PLANEJAR_METAS_EMPLANEJAMENTO = 184;
	public static final int COR_FUNDO_CEDULA_PLANEJAR_METAS_METAPLANEJADA_SUPERIOR_METACONTRATADA = 185;
	public static final int COR_FUNDO_CEDULA_PLANEJAR_METAS_METAPLANEJADA_INFERIOR_METACONTRATADA = 186;
	public static final int COR_FUNDO_LISTA_PLANEJAR_METAS_ENCERRADO = 187;
	public static final int COR_FUNDO_ITEM_PRODUTO_RESTRITO = 188;
	public static final int COR_FUNDO_ITEM_PEDIDO_COMBO = 189;
	public static final int COR_FUNDO_PEDIDO_NAO_AUTORIZADO = 190;
	public static final int COR_FUNDO_ITEM_PEDIDO_NAO_AUTORIZADO = 191;
	public static final int COR_FUNDO_ITEM_COMBO_ESTOQUE = 192;

	public static final int COR_FUNDO_KIT_SEM_ESTOQUE = 193;
	public static final int COR_PRODUTO_INSERIDO_DESC_PROGRESSIVO_BACK = 195; 
	public static final int COR_FUNDO_PRODUTO_AUTORIZADO_OU_DISTRIBUIDO = 196;
	public static final int COR_FUNDO_GRID_BONIFCFG_OBRIGATORIA_SALDO_PENDENTE = 197;
	public static final int COR_FUNDO_ITEM_SEM_MULTIPLO = 198;
	public static final int COR_FUNDO_GRID_ITEMBONIFCFG_BRINDE = 199;
	public static final int COR_FUNDO_GRID_ITEMBONIFCFG_BRINDE_OBRIGATORIO = 200;
	
	public static final int COR_FUNDO_GRID_PRODUTO_BLOQUEADO = 201;

	public static final int COR_FUNDO_ITEMKIT_BONIFICADO = 202;
}