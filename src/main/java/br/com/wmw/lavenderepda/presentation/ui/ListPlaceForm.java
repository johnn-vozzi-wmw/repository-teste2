package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditNumber;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.sync.transport.http.HttpSync;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ListaLeads;
import br.com.wmw.lavenderepda.business.domain.Place;
import br.com.wmw.lavenderepda.business.domain.dto.ListaLeadsDTO;
import br.com.wmw.lavenderepda.business.service.ListaLeadsService;
import br.com.wmw.lavenderepda.business.service.PlaceService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CategoriaLeadsComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoQualificacaoPlacesBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Window;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListPlaceForm extends LavendereCrudListForm {

	private static final int VALOR_LIMITE_GOOGLE_MAPS = 10;

	private CategoriaLeadsComboBox cbCategoria;
	private EditNumber edVlRaio;
	private EditText edNmLista;
	private EditText edTextFilter;
	private EditText edEnderecoFilter;
	private ButtonAction btSalvar;
	private ButtonAction btBuscarLista;
	private ListaLeads listaLeads;
	private LabelValue lbNmLista;
	private ButtonAction btMapaGoogleMaps;
	private ButtonAction btMapaTotalCross;
	private TipoQualificacaoPlacesBox cbQualificacaoPlacesBox;

	public ListPlaceForm() throws SQLException {
		super(Messages.LISTALEADS_NOME_ENTIDADE);
		instantiateComponents();
	}

	public ListPlaceForm(ListaLeads listaLeads) throws SQLException {
		super(Messages.LISTALEADS_NOME_ENTIDADE);
		this.listaLeads = listaLeads;
		instantiateComponents();
		loadDomainList();
	}

	private void instantiateComponents() throws SQLException {
		if (LavenderePdaConfig.isUsaCategoriaLeads()) {
			cbCategoria = new CategoriaLeadsComboBox();
			cbCategoria.setSelectedIndex(0);
		}
		edVlRaio = new EditNumberInt("9999999", 5);
		edVlRaio.setValue("1000");
		lbNmLista = new LabelValue(listaLeads != null ? listaLeads.nmLista : ValueUtil.VALOR_NI);
		edNmLista = new EditText("", 200);
		edTextFilter = new EditText("", 200);
		if (VmUtil.isSimulador()) {
			edEnderecoFilter = new EditText("", 200);
		}
		btSalvar = new ButtonAction(Messages.LABEL_BT_SALVAR, "images/success.png");
		btSalvar.setVisible(false);
		btBuscarLista = new ButtonAction(Messages.PLACES_BUSCA_LISTA, "images/reload.png");
		btMapaGoogleMaps = new ButtonAction(Messages.BOTAO_ROTA, "images/mapagoogle.png");
		btMapaTotalCross = new ButtonAction(Messages.BOTAO_MAPA_CLIENTE, "images/mapatotalcross.png");
		listContainer = new GridListContainer(8, 2);
		if (listaLeads != null) {
			listContainer.setColsSort(new String[][] { { Messages.LISTALEADS_SORT_PROXIMIDADE, Place.NMCOLUNA_NUORDEM }, { Messages.LISTALEADS_SORT_NMPLACE, Place.NMCOLUNA_NMPLACE } });
		}
		listContainer.setCheckable(true);
		cbQualificacaoPlacesBox = new TipoQualificacaoPlacesBox();
		cbQualificacaoPlacesBox.setSelectedIndex(0);
		edFiltro.setValue(ValueUtil.VALOR_NI);
		configListContainer(Place.NMCOLUNA_NMPLACE);
		singleClickOn = true;
	}

	private void btBuscarListaClick() {
		try {
			ListaLeadsDTO leadsDTO = new ListaLeadsDTO();
			leadsDTO.raio = edVlRaio.getValue();
			if (LavenderePdaConfig.isUsaCategoriaLeads()) {
				leadsDTO.categoria = cbCategoria.getValue();
			}
			leadsDTO.textFilter = edTextFilter.getValue().replace(" ", "_");
			if (ValueUtil.isEmpty(leadsDTO.textFilter)) {
				UiUtil.showWarnMessage(Messages.LISTALEADS_FILTRO_VAZIO);
				return;
			} else if (ValueUtil.isEmpty(leadsDTO.raio)) {
				UiUtil.showWarnMessage(Messages.LISTALEADS_INFO_RAIO);
				return;
			} else if (ValueUtil.getIntegerValue(leadsDTO.raio) < 1 || ValueUtil.getIntegerValue(leadsDTO.raio) > 50000) {
				UiUtil.showWarnMessage(Messages.LISTALEADS_RAIO_MAXIMO);
				return;
			} else if (ValueUtil.isEmpty(edNmLista.getValue())) {
				UiUtil.showWarnMessage(Messages.LISTALEADS_LISTA_NOME);
				return;
			} else if (VmUtil.isSimulador() && ValueUtil.isEmpty(edEnderecoFilter.getValue())) {
				UiUtil.showWarnMessage(Messages.LISTALEADS_LISTA_ENDERECO);
				return;
			}
			if (VmUtil.isSimulador()) {
				leadsDTO.enderecoFilter = edEnderecoFilter.getValue().replace(" ", "_");
			} else {
				GpsData gpsData = ListaLeadsService.getInstance().getCoordenadaAtual(true);
				if (gpsData == null) {
					UiUtil.showErrorMessage(Messages.LISTALEADS_ERRO_GPS);
					return;
				}
				leadsDTO.cdLatitude = gpsData.latitude;
				leadsDTO.cdLongitude = gpsData.longitude;
			}

			UiUtil.showProcessingMessage();
			montaNovaListaLeads();
			listaLeads.placesList = PlaceService.getInstance().buscarLista(leadsDTO);
			btSalvar.setVisible(!listaLeads.placesList.isEmpty());
			loadDomainList();
			list();
			UiUtil.unpopProcessingMessage();
		} catch (ValidationException ex) {
			UiUtil.unpopProcessingMessage();
			UiUtil.showErrorMessage(ex.getMessage());
		} catch (Exception e) {
			UiUtil.unpopProcessingMessage();
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.LISTALEADS_ERRO_LEADS, e.getMessage()));
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	public void montaNovaListaLeads() throws SQLException {
		listaLeads = new ListaLeads();
		listaLeads.nmLista = edNmLista.getValue();
		listaLeads.cdEmpresa = SessionLavenderePda.cdEmpresa;
		listaLeads.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		listaLeads.flTpCadastro = ListaLeads.LEADS_TIPO_PDA;
		listaLeads.dtCriacao = new Date();
		listaLeads.cdUsuario = Session.getCdUsuario();
	}

	private void btSalvarClick() throws SQLException {
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		if (gridSize < 1) {
			UiUtil.showWarnMessage(Messages.LIMITE_MINIMO_SALVAR_LEADS);
			return;
		}
		listaLeads.nmLista = edNmLista.getValue();
		listaLeads.cdLista = ListaLeadsService.getInstance().generateIdGlobal();
		ListaLeadsService.getInstance().insert(listaLeads);
		int listSize = listaLeads.placesList.size();

		for (int i = 0; i < gridSize; i++) {
			for (int x = 0; x < listSize; x++) {
				Place place = (Place) listaLeads.placesList.items[x];
				if (place.getRowKey().equals(listContainer.getId(checkedItens[i]))) {
					place.cdLista = listaLeads.cdLista;
					PlaceService.getInstance().insertOrUpdate(place);
				}
			}
		}
		close();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PlaceService.getInstance();
	}

	protected BaseDomain getDomainFilter() throws SQLException {
		if (listaLeads != null) {
			Place placeFilter = getPlaceFilter();
			placeFilter.dsFiltro = edFiltro.getValue();
			return placeFilter;
		}
		return new Place();
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (listaLeads != null) {
			UiUtil.add(barBottomContainer, btMapaTotalCross, 4);
			UiUtil.add(barBottomContainer, btMapaGoogleMaps, 5);
			UiUtil.add(this, new LabelName(Messages.LISTALEADS_LABEL_NMLISTA), lbNmLista, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_QUALIFICACAO), cbQualificacaoPlacesBox, getLeft(), getNextY());
			UiUtil.add(this, edFiltro, getLeft(), getNextY());
			UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
		} else {
			UiUtil.add(barBottomContainer, btBuscarLista, 4);
			UiUtil.add(barBottomContainer, btSalvar, 5);
			UiUtil.add(this, new LabelName(Messages.LISTALEADS_LABEL_NMLISTA + "*"), edNmLista, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.LISTALEADS_LABEL_FILTRO), edTextFilter, getLeft(), getNextY());
			if (VmUtil.isSimulador()) {
				UiUtil.add(this, new LabelName(Messages.PLACES_LABEL_ENDERECO + "*"), edEnderecoFilter, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.isUsaCategoriaLeads()) {
				UiUtil.add(this, new LabelName(Messages.LISTALEADS_LABEL_CATEGORIA), cbCategoria, getLeft(), getNextY());
			}
			UiUtil.add(this, new LabelName(Messages.LISTALEADS_LABEL_RAIO), edVlRaio, getLeft(), getNextY());
			UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
		}
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Place place = (Place) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(place.nmPlace));
		item.addElement(FrameworkMessages.CAMPO_VAZIO);
		item.addElement(StringUtil.getStringValue(place.dsEndereco));
		item.addElement(FrameworkMessages.CAMPO_VAZIO);
		if (LavenderePdaConfig.isApresentaTelefoneListaLeadsLigado()) {
			item.addElement(Messages.PLACES_LABEL_TELEFONE + ": " + StringUtil.getStringValue(place.dsTelefone));
		} else {
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
		}
		item.addElement(FrameworkMessages.CAMPO_VAZIO);
		if (LavenderePdaConfig.isApresentaSiteListaLeadsLigado()) {
			item.addElement(Messages.PLACES_LABEL_SITE + ": " + StringUtil.getStringValue(place.dsUrl));
		} else {
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
		}
		item.addElement(FrameworkMessages.CAMPO_VAZIO);
		
		return (String[]) item.toObjectArray();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btSalvar) {
				btSalvarClick();
			} else if (event.target == btBuscarLista) {
				btBuscarListaClick();
			} else if (event.target == btMapaGoogleMaps) {
				btMapaGoogleMapsClick();
			} else if (event.target == btMapaTotalCross) {
				btMapaGoogleApiClick();
			}
		}
		case ValueChangeEvent.VALUE_CHANGE: {
			if (event.target == cbQualificacaoPlacesBox) {
				list();
			}
			break;
		}
		}
	}

	protected Vector loadDomainList() throws SQLException {
		if (listaLeads != null) {
			if (listaLeads.placesList != null) {
				return listaLeads.placesList;
			} else {
				Place placeFilter = getPlaceFilter();
				placeFilter.sortAtributte = sortAtributte;
				placeFilter.sortAsc = sortAsc;
				listaLeads.placesList = PlaceService.getInstance().findAllByExample(placeFilter);
				return listaLeads.placesList;
			}
		}
		return new Vector();
	}

	private Place getPlaceFilter() {
		Place placeFilter = new Place();
		placeFilter.cdEmpresa = listaLeads.cdEmpresa;
		placeFilter.cdRepresentante = listaLeads.cdRepresentante;
		placeFilter.cdLista = listaLeads.cdLista;
		placeFilter.flQualificado = cbQualificacaoPlacesBox.getValue();
		return placeFilter;
	}

	@Override
	protected void filtrarClick() throws SQLException {
		list();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		if (listaLeads == null) {
			return new Vector();
		} else if (listaLeads.cdLista == null) {
			return listaLeads.placesList;
		}
		return super.getDomainList(domain);
	}

	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		Place place = (Place) domain;
		if (ValueUtil.isNotEmpty(place.flQualificado)) {
			if (Place.TIPO_NAO_QUALIFICADO.equals(place.flQualificado)) {
				listContainer.setContainerBackColor(c, getColor(LavendereColorUtil.softRed));
			} else if (Place.TIPO_QUALIFICADO.equals(place.flQualificado)) {
				listContainer.setContainerBackColor(c, getColor(LavendereColorUtil.softGreen));
			}
		}
	}
	
	private int getColor(int color) {
		return ColorUtil.isLightTheme() ? Color.brighter(color, 136) : Color.darker(color, 136);
	}
	
	@Override
	public void singleClickInList() throws SQLException {
		Place place = (Place) super.getSelectedDomain();
		if (place == null) {
			BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
			int listSize = listaLeads.placesList.size();
			for (int i = 0; i < listSize; i++) {
				Place placeAux = (Place) listaLeads.placesList.items[i];
				if (c.id.contains(placeAux.cdPlaceId)) {
					place = placeAux;
				}
			}
			
		}
		CadPlaceWindow cadPlaceWindow = new CadPlaceWindow(place);
		cadPlaceWindow.edit(place);
		cadPlaceWindow.popup();
		if (cadPlaceWindow.houveMudanca) {
			updateCurrentRecord(place);
			Window.needsPaint = true;
		}
	}

	private void btMapaGoogleMapsClick() throws SQLException {
		if (VmUtil.isSimulador()) {
			UiUtil.showWarnMessage(Messages.LEADS_SERVICO_NAO_SUPORTADO_NO_SIMULADOR);
			return;
		}
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		if (gridSize < 1) {
			UiUtil.showWarnMessage(Messages.LIMITE_MINIMO_MAPS_LEADS);
			return;
		}
		Vector placesList = new Vector();
		Place place = new Place();
		GpsData gpsData = ListaLeadsService.getInstance().getCoordenadaAtual(true);
		if (ValueUtil.isEmpty(gpsData)) {
			return;
		}
		place.cdLatitude = gpsData.latitude;
		place.cdLongitude = gpsData.longitude;

		placesList.addElement(place);
		for (int i = 0; i < gridSize; i++) {
			placesList.addElement(PlaceService.getInstance().findByRowKeyDyn(listContainer.getId(checkedItens[i])));
		}
		if (ValueUtil.isEmpty(placesList)) {
			UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_SEM_COORDENADA);
			return;
		}
		if (placesList.size() > 10) {
			UiUtil.showInfoMessage(Messages.LIMITACAO_GOOGLE_MAPS_LEADS);
		} else {
			mostrarMapaGoogleMaps(placesList);
		}
	}

	private void mostrarMapaGoogleMaps(Vector placesList) {
		if (placesList == null || placesList.size() == 0) {
			return;
		}
		String coordenadaUrl = "";
		if (placesList.size() == 1) {
			coordenadaUrl = generateUrlForOnePlace(placesList.items[0]);
		} else {
			coordenadaUrl = getCoordenadasUrl(placesList);
		}
		if (!ValueUtil.valueEquals(coordenadaUrl, ValueUtil.VALOR_NI)) {
			VmUtil.debug("Retorno da abertura mapa geolocalizacao:" + UiUtil.openBrowser(coordenadaUrl));
		}
	}

	private String getCoordenadasUrl(Vector placesList) {
		StringBuffer strbfUrl = new StringBuffer();
		int size = placesList.size();
		int index = 2;
		generateUrlGoogle(placesList.items[0], placesList.items[1], strbfUrl);
		while (index < VALOR_LIMITE_GOOGLE_MAPS && index < size) {
			Place place = (Place) placesList.items[index++];
			strbfUrl.append("%20to:").append(place.cdLatitude).append(",").append(place.cdLongitude);
		}
		return strbfUrl.toString();
	}

	private void generateUrlGoogle(Object primeiroPlaceObject, Object segundoPlaceObject, StringBuffer strbfUrl) {
		Place primeiroPlace = (Place) primeiroPlaceObject;
		Place segundoPlace = (Place) segundoPlaceObject;
		generateDefaultUrlGoogle(primeiroPlace.cdLatitude, primeiroPlace.cdLongitude, segundoPlace.cdLatitude,
				segundoPlace.cdLongitude, strbfUrl);
	}

	private String generateUrlForOnePlace(Object primeiroPlace) {
		GpsData coordenadaLocalAtual = ListaLeadsService.getInstance().getCoordenadaAtual(false);
		if (coordenadaLocalAtual == null) {
			return "";
		}
		Place place = (Place) primeiroPlace;
		StringBuffer strbfUrl = new StringBuffer();
		generateDefaultUrlGoogle(coordenadaLocalAtual.latitude, coordenadaLocalAtual.longitude, place.cdLatitude,
				place.cdLongitude, strbfUrl);
		return strbfUrl.toString();
	}

	private void generateDefaultUrlGoogle(double cdLatitude, double cdLongitude, double cdLatitude2, double cdLongitude2, StringBuffer strbfUrl) {
		strbfUrl.append("http://maps.google.com/maps?saddr=").append(cdLatitude).append(",").append(cdLongitude);
		strbfUrl.append("&daddr=").append(cdLatitude2).append(",").append(cdLongitude2);
	}

	private void btMapaGoogleApiClick() throws SQLException {
		if (VmUtil.isSimulador()) {
			UiUtil.showWarnMessage(Messages.LEADS_SERVICO_NAO_SUPORTADO_NO_SIMULADOR);
			return;
		}
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		if (gridSize < 1) {
			UiUtil.showWarnMessage(Messages.LIMITE_MINIMO_MAPS_LEADS);
			return;
		}
		Vector placesList = new Vector();
		for (int i = 0; i < gridSize; i++) {
			placesList.addElement(PlaceService.getInstance().findByRowKeyDyn(listContainer.getId(checkedItens[i])));
		}
		if (!testeConexao()) {
			UiUtil.showWarnMessage(Messages.AGENDA_VISITA_CLIENTE_NO_MAPA_ERRO);
			return;
		}
		if (ValueUtil.isEmpty(placesList)) {
			UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_SEM_COORDENADA);
			return;
		}
		mostrarMapaGoogleApi(placesList);
	}

	private boolean testeConexao() {
		try {
			HttpSync httpSync = new HttpSync();
			int responseCode = httpSync.testConnection("http://www.google.com");
			return responseCode == 200;
		} catch (Exception ex) {
			return false;
		}
	}

	private void mostrarMapaGoogleApi(Vector placesList) {
		try {
			String coordenadasDosPlaces = getCoordenadaPlace(placesList);
			if (ValueUtil.isEmpty(coordenadasDosPlaces)) {
				UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_SEM_COORDENADA);
				return;
			}
			String retorno = SyncManager.postCoodernadasClient(coordenadasDosPlaces);
			if (ValueUtil.isNotEmpty(retorno) && retorno.startsWith("OK")) {
				String link = montarLink(HttpConnectionManager.getDefaultParamsSync());
				UiUtil.openBrowser(link);
			} else {
				UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_SEM_COORDENADA);
			}
		} catch (Exception e) {
			UiUtil.showErrorMessage(e.getMessage());
		}
	}

	private String montarLink(ParamsSync paramsSync) {
		return paramsSync.baseUrl + paramsSync.basePublicUrl + "marcadormapaapp.faces?chave=" + SessionLavenderePda.usuarioPdaRep.usuario;
	}

	private String getCoordenadaPlace(Vector placesList) {
		int size = placesList.size();
		String coordenadaPlace = "";
		String vir = "";
		for (int i = 0; i < size; i++) {
			Place place = (Place) placesList.items[i];
			if (place.cdLatitude != 0.0 && place.cdLongitude != 0.0) {
				coordenadaPlace += vir + "[{\"lat\":" + place.cdLatitude + ",\"lng\":" + place.cdLongitude + "},\"" + getNmRazaoSocial(place.nmPlace) + "\"]";
				vir = ",";
			}
		}
		if (!"".equals(coordenadaPlace)) {
			coordenadaPlace = "[" + coordenadaPlace + "]";
			return coordenadaPlace;
		} else {
			return "";
		}
	}

	private String getNmRazaoSocial(String nmRazaoSocial) {
		if (nmRazaoSocial.length() > 20) {
			return nmRazaoSocial.substring(0, 20);
		} else {
			return nmRazaoSocial;
		}
	}

}
