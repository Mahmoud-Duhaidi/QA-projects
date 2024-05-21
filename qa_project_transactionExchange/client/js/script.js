"use strict";

import countryList from "./countryList.js";

const fromCurrencySelect = document.getElementById("fromCurrency");
const toCurrencySelect = document.getElementById("toCurrency");
const currencyForm = document.getElementById("currency-form");
const resultToForm = document.getElementById("result");
const rateUsed = document.getElementById("rateUsed");
const amount = document.getElementById("amount");

document.addEventListener("DOMContentLoaded", () => {
  // addCurrencyOptions(fromCurrencySelect, countryList, excludedCurrencies);
  // addCurrencyOptions(toCurrencySelect, countryList, excludedCurrencies, 1);

  populateDropdowns();

  // Get dropdown elements
  const fromCurrencySelect = document.getElementById("fromCurrency");
  const toCurrencySelect = document.getElementById("toCurrency");

  // Event listener for 'fromCurrency' dropdown
  fromCurrencySelect.addEventListener("change", function () {
    filterOptions(this.value, toCurrencySelect);
  });

  // Event listener for 'toCurrency' dropdown
  toCurrencySelect.addEventListener("change", function () {
    filterOptions(this.value, fromCurrencySelect);
  });

  currencyForm.addEventListener("submit", (event) => {
    event.preventDefault();
    setTimeout(() => {
      dataFromToConversionRate();
      getRate();
    }, 300);
  });
});

const changeInput = function (amountin) {
  console.log(typeof amountin);

  if (amountin.includes(",")) {
    amount.value = amountin.replace(",", ".");
  }
};

///=======================================================================================
function populateDropdowns() {
  const fromCurrencySelect = document.getElementById("fromCurrency");
  const toCurrencySelect = document.getElementById("toCurrency");

  for (const [currencyCode, countryCode] of Object.entries(countryList)) {
    // Create new option element for 'fromCurrency'
    let fromOption = document.createElement("option");
    fromOption.value = currencyCode;
    fromOption.textContent = currencyCode; // or any other text you want to show
    fromCurrencySelect.appendChild(fromOption);

    // Create new option element for 'toCurrency'
    let toOption = document.createElement("option");
    toOption.value = currencyCode;
    toOption.textContent = currencyCode; // or any other text you want to show
    toCurrencySelect.appendChild(toOption);
  }

  fromCurrencySelect.value = "USD";
  filterOptions(fromCurrencySelect.value, toCurrencySelect);
  loadFlag(fromCurrencySelect);
  toCurrencySelect.value = "ILS";
  filterOptions(toCurrencySelect.value, fromCurrencySelect);
  loadFlag(toCurrencySelect);
}

function filterOptions(selectedValue, dropdownToFilter) {
  // Iterate over the options in dropdownToFilter
  for (const option of dropdownToFilter.options) {
    if (option.value === selectedValue) {
      // Disable the option that matches the selected value
      option.disabled = true;
    } else {
      // Enable all other options
      option.disabled = false;
    }
  }
}

//========================================================================================================

const dataFromToConversionRate = async function () {
  changeInput(amount.value);
  console.log(amount.value);
  try {
    const response = await fetch("http://127.0.0.1:8080/api/qa/convert", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        fromCurrency: fromCurrencySelect.value,
        toCurrency: toCurrencySelect.value,
        amount: amount.value,
      }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const { convertedAmount } = await response.json();
    resultToForm.value = convertedAmount.toFixed(2);

    checkInput(convertedAmount);
    clearUI();
  } catch (error) {
    console.log(error);
    Swal.fire({
      icon: "error",
      title: "Can't Convert Currencies!",
      text: "there is no feedback data to be converted between those countries",
    });
  }
};

const getRate = async function () {
  const response = await fetch(
    `http://127.0.0.1:8080/api/qa/getRate?fromCurrency=${fromCurrencySelect.value}&toCurrency=${toCurrencySelect.value}`
  );

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  const rate = await response.json();
  const { convertedRate } = rate;

  rateUsed.value = convertedRate.toFixed(5);
};

// Function to load flag image based on selected currency
const loadFlag = function (element) {
  const currencyCode = element.value;
  const countryIsoCode = countryList[currencyCode].toLowerCase();
  const imgTag = element.parentElement.querySelector("img");
  imgTag.src = `https://flagcdn.com/w40/${countryIsoCode}.png`;
  imgTag.alt = `${currencyCode} Flag`;
};

// Ensure this function is available globally if it's being called inline from the HTML
window.loadFlag = loadFlag;

const checkInput = function (input) {
  if (Number.isNaN(Number.parseInt(input))) {
    Swal.fire({
      icon: "error",
      title: "Error Type Format !",
      text: "For this country please use '123.456' (dot) format rather than '123,456'(comma)",
    });
    clearUI2();
  }
};

const clearUI = function () {
  amount.value = "";
};

const clearUI2 = function () {
  resultToForm.value = "";
  rateUsed.value = "";
};
