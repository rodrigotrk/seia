package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.DualListModel;

import br.gov.ba.seia.entity.Player;

@Named("pickListUnMedidaController")
@RequestScoped
public class PickListUnMedidaController {
	
	 private DualListModel<Player> players;

		private DualListModel<String> cities;

		public PickListUnMedidaController() {
	
			List<String> citiesSource = new ArrayList<String>();
			List<String> citiesTarget = new ArrayList<String>();

			citiesSource.add("Cabeça (Un)");
			citiesSource.add("Área Cultivada (Ha)");
			citiesSource.add("Matrizes (Un)");
			citiesSource.add("Área Total (m²)");
			citiesSource.add("Bursa");

			cities = new DualListModel<String>(citiesSource, citiesTarget);
		}

		public DualListModel<Player> getPlayers() {
			return players;
		}
		public void setPlayers(DualListModel<Player> players) {
			this.players = players;
		}

		public DualListModel<String> getCities() {
			return cities;
		}
		public void setCities(DualListModel<String> cities) {
			this.cities = cities;
		}
	
	

}
