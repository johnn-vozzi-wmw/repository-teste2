package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Place;
import br.com.wmw.lavenderepda.business.service.PlaceService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoQualificacaoPlacesBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadPlaceWindow extends WmwCadWindow {

    private LabelValue lbNome;
    private LabelValue lbEndereco;
    private LabelValue lbTelefone;
    private LabelValue lbSite;
    private LabelValue lbRating;
    private EditMemo edObs;
    private TipoQualificacaoPlacesBox cbQualificacaoPlacesBox;
	public boolean houveMudanca;
	
	public CadPlaceWindow(Place place) {
		super(Messages.PLACES_DETALHES);
		setDomain(place);
		cbQualificacaoPlacesBox = new TipoQualificacaoPlacesBox(true);
		lbNome = new LabelValue();
		lbEndereco = new LabelValue();
		lbTelefone = new LabelValue();
		lbSite = new LabelValue();
		lbRating = new LabelValue();
		edObs = new EditMemo("@@@@@@@@@@@@", 3, 1, 250).setID("edObs");
		houveMudanca = false;
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		Place place = getDomainSafe();
		UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_NOME), lbNome, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_ENDERECO), lbEndereco, getLeft(), getNextY());
		lbEndereco.setMultipleLinesText(place.dsEndereco);
		if (LavenderePdaConfig.isApresentaTelefoneListaLeadsLigado()) {
			UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_TELEFONE), lbTelefone, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isApresentaSiteListaLeadsLigado()) {
			UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_SITE), lbSite, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isApresentaNotaListaLeadsLigado()) {
			UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_RATING), lbRating, getLeft(), getNextY());
		}
		if (place.cdLista != null) {
			UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_QUALIFICACAO), cbQualificacaoPlacesBox, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_OBS), edObs, getLeft(), getNextY());
		} else {
			place.flQualificado = Place.TIPO_EM_ANALISE;
		}
	}

	private Place getDomainSafe() {
		try {
			return (Place) getDomain();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return new Place();
		}
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {	
		Place place = (Place) getDomain();
		place.flQualificado = cbQualificacaoPlacesBox.getValue();
		place.dsObservacao = edObs.getText();
		return place; 
	}
	
	@Override
	protected void onSave() throws SQLException {
		setDomain(screenToDomain());
		Place place = (Place) getDomain();
		if (place.flQualificado.equals(Place.TIPO_NAO_QUALIFICADO) && place.dsObservacao.trim().isEmpty()) {
			place.flQualificado = Place.TIPO_EM_ANALISE;
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PLACES_LABEL_OBS);
		} else if (place.flQualificado.equals(Place.TIPO_NAO_QUALIFICADO)) {
			place.dtInativacao = DateUtil.getCurrentDate();
		}
		if (place.cdLista != null) {
			insertOrUpdate(getDomain());
			houveMudanca = true;
		}
		fecharWindow();
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		Place place = (Place) domain;
		lbNome.setValue(place.nmPlace);
		lbTelefone.setValue(StringUtil.getStringValue(place.dsTelefone));
		lbSite.setValue(StringUtil.getStringValue(place.dsUrl));
		lbRating.setValue(StringUtil.getStringValue(place.vlRating));
		edObs.setValue(StringUtil.getStringValue(place.dsObservacao));
		cbQualificacaoPlacesBox.setValue(place.flQualificado);
	}
	
	@Override
	protected void clearScreen() throws SQLException {
		// Desnecessário
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new Place();
	}

	@Override
	protected String getEntityDescription() {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PlaceService.getInstance();
	}
	
	@Override
	protected int getTop() {
		return TOP;
	}

	@Override
	protected void btFecharClick() throws SQLException {
		houveMudanca = false;
		fecharWindow();
	}

	@Override
	protected void addButtons() {
		btSalvar.setText(FrameworkMessages.BOTAO_SALVAR);
		addButtonPopup(btSalvar);
		btFechar.setText(FrameworkMessages.BOTAO_CANCELAR);
		addButtonPopup(btFechar);
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		try {
			switch (event.type) {
			case ControlEvent.PRESSED: {
				break;
			}
			}
		} catch (Exception ex) {
			ExceptionUtil.handle(ex);
		}
	}

}
