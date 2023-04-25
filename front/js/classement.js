$(document).ready(function() {
  // Initialiser le tableau DataTable en français et trier sur la première colonne
  var dataTable = $("#allUsers").DataTable({
    language: {
      url: "https://cdn.datatables.net/plug-ins/1.11.2/i18n/French.json"
    },
    columns: [
      { title: "ID", data: "id" },
      { title: "Nom", data: "username" },
      { title: "Balance", data: "balance" }
    ],
    order: [[2, "desc"]]
  });

  // Faire une requête AJAX pour récupérer les données JSON
  $.ajax({
    url: "http://localhost:8888/users",
    type: "GET",
    dataType: "json",
    success: function(data) {
      // Ajouter les données JSON récupérées au tableau DataTable
      dataTable.rows.add(data).draw();
    },
    error: function(jqXHR, textStatus, errorThrown) {
      console.log(textStatus, errorThrown);
    }
  });
});
