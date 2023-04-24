// $(document).ready(function () {
//   $.ajax({
//     url: "http://localhost:8888/users/1",
//     type: "GET",
//     dataType: "json",
//     success: function (data) {
//       var html = "";
//       for (var i = 0; i < data.length; i++) {
//         html += "<tr>";
//         html += "<td>" + data[i].id + "</td>";
//         html += "<td>" + data[i + 1].balance + "$" + "</td>";
//         html += "</tr>";
//       }
//       $("#user-info").html(data.username);
//       $("#money").html(data.balance);
//     },
//   });
// });

function createTable(data) {
  const tableBody = document.querySelector('#rooms tbody');

  data.forEach((room) => {
    const row = document.createElement('tr');

    row.appendChild(createElement('td', room.id));
    row.appendChild(createElement('td', room.name));
    row.appendChild(createElement('td', room.idUser_1));
    row.appendChild(createElement('td', room.idUser_2));
    row.appendChild(createElement('td', room.idCardUser_1));
    row.appendChild(createElement('td', room.idCardUser_2));
    row.appendChild(createElement('td', room.cooldownUser_1));
    row.appendChild(createElement('td', room.cooldownUser_2));
    row.appendChild(createElement('td', room.status));
    row.appendChild(createElement('td', room.idUserWinner));
    row.appendChild(createElement('td', room.reward));

    tableBody.appendChild(row);
  });
}

function createElement(tagName, textContent) {
  const element = document.createElement(tagName);
  element.textContent = textContent;
  return element;
}

async function fetchRooms() {
  try {
    const response = await fetch('http://localhost:8888/rooms');
    const data = await response.json();
    createTable(data);
  } catch (error) {
    alert('Erreur lors de la récupération des informations de la room.');
  }
}

document.addEventListener('DOMContentLoaded', fetchRooms);
