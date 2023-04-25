const getCardData = async (cardId) => {
  try {
    const response = await fetch(`http://localhost:8888/cards/${cardId}`);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error(`Error fetching card data: ${error}`);
    throw error;
  }
};

const getInventoryData = async (userId) => {
  try {
    const response = await fetch(
      `http://localhost:8888/inventories/users/${userId}`,
    );
    const data = await response.json();
    return data;
  } catch (error) {
    console.error(`Error fetching inventory data: ${error}`);
    throw error;
  }
};

const createTableCell = (data) => {
  const cell = document.createElement('td');
  cell.textContent = data;
  return cell;
};

const createTableRow = (cardData) => {
  const row = document.createElement('tr');

  row.appendChild(createTableCell(cardData.id));
  row.appendChild(createTableCell(cardData.name));
  row.appendChild(createTableCell(cardData.description));
  row.appendChild(createTableCell(cardData.power));
  row.appendChild(createTableCell(cardData.health));
  row.appendChild(createTableCell(cardData.type));
  row.appendChild(createTableCell(cardData.energy));
  row.appendChild(createTableCell(cardData.price));

  const imageCell = document.createElement('td');
  const image = document.createElement('img');
  image.setAttribute('src', cardData.image);
  image.setAttribute('alt', cardData.name);
  imageCell.appendChild(image);
  row.appendChild(imageCell);

  const buyButtonCell = document.createElement('td');
  const buyButton = document.createElement('button');
  buyButton.textContent = 'Acheter';
  buyButtonCell.appendChild(buyButton);
  row.appendChild(buyButtonCell);

  const sellButtonCell = document.createElement('td');
  const sellButton = document.createElement('button');
  sellButton.textContent = 'Vendre';
  sellButtonCell.appendChild(sellButton);
  row.appendChild(sellButtonCell);

  return row;
};

const displayInventory = async () => {
  const inventoryData = await getInventoryData(1);
  const inventoryCards = inventoryData.inventory.cards;
  console.log(inventoryCards);
  for (const card of inventoryCards) {
    console.log(card);
    const cardData = await getCardData(card);
    const tableRow = createTableRow(cardData);
    document.querySelector('#account tbody').appendChild(tableRow);
  }
};

document.addEventListener('DOMContentLoaded', () => {
  displayInventory();
});
