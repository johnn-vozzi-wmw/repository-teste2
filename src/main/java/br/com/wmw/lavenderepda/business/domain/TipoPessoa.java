package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class TipoPessoa extends BaseDomain {

    public String flTipoPessoa;
    public String dsTipoPessoa;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoPessoa) {
            TipoPessoa tipoPessoa = (TipoPessoa) obj;
            return
                flTipoPessoa.equals(tipoPessoa.flTipoPessoa);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	return String.valueOf(flTipoPessoa);
    }

    @Override
    public String toString() {
    	return dsTipoPessoa;
    }

}