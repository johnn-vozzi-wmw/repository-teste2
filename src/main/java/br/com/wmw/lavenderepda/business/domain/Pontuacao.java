package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Pontuacao extends BaseDomain {

    public static String TABLE_NAME = "TBLVPPONTUACAO";

    public String cdEmpresa;
    public String cdRepresentante;
    public double vlPctLucro;
    public int qtPontos;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Pontuacao) {
            Pontuacao pontuacao = (Pontuacao) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pontuacao.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, pontuacao.cdRepresentante) &&
                ValueUtil.valueEquals(vlPctLucro, pontuacao.vlPctLucro);
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
        primaryKey.append(vlPctLucro);
        return primaryKey.toString();
    }

}