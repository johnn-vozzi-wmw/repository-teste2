package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.json.JSONObject;

import java.util.LinkedHashMap;

public class MenuCatalogo extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPMENUCATALOGO";
    public static final String NM_COLUNA_NMFOTO = "NMFOTO";

    public String cdFuncionalidade;
    public String nmEntidade;
    public String nmMenu;
    public String dsSql;
    public int nuNivel;
    public String flIndividual;
    //Nao persistente
    public LinkedHashMap<String, String> valoresFiltroTelaPaiHash;
    public String nmColunaDescricao;
    public String dsFiltro;
    public JSONObject pedidoJsonFilters;

    public MenuCatalogo() {
        valoresFiltroTelaPaiHash = new LinkedHashMap<>();
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(cdFuncionalidade);
        strBuffer.append(";");
        strBuffer.append(nuNivel);
        return strBuffer.toString();
    }
    
    public boolean isIndividual() {
        return ValueUtil.VALOR_SIM.equalsIgnoreCase(flIndividual);
    }
}
