package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PreferenciaFuncao extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPREFERENCIAFUNCAO";
	
	public static final int PERMITE_ALTERACAO_TIPO_PEDIDO = 22;
	public static final int PERMITE_USO_PERIODO_ENTREGA_RESTRITO = 25;
	public static final int BLOQUEIA_EMISSAO_PEDIDO_SMARTPHONE = 29;
	public static final int PERMITE_TIPOPAGAMENTO_IGNORA_LIMITE_CREDITO = 31;

    public int cdSistema;
    public int cdPreferencia;
    public String cdFuncao;
    public Date dtAlteracao;
    public String hrAlteracao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PreferenciaFuncao) {
            PreferenciaFuncao preferenciafuncao = (PreferenciaFuncao) obj;
            return
                ValueUtil.valueEquals(cdSistema, preferenciafuncao.cdSistema) && 
                ValueUtil.valueEquals(cdPreferencia, preferenciafuncao.cdPreferencia) && 
                ValueUtil.valueEquals(cdFuncao, preferenciafuncao.cdFuncao);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdSistema);
        primaryKey.append(";");
        primaryKey.append(cdPreferencia);
        primaryKey.append(";");
        primaryKey.append(cdFuncao);
        return primaryKey.toString();
    }

}