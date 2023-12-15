const cart = parseInt(document.getElementById("cartId").value);
function reloadAfterTenMinutes() {
    setTimeout(() => {
        deleteCart(cart);
    }, 600000);
}
reloadAfterTenMinutes();

function addProductToCart(productId, productDescription, productPrice, amountElementId, maxAmount) {
    var currentAmount = parseInt(document.getElementById(amountElementId).innerHTML);
    currentAmount++;
    if (currentAmount <= maxAmount) {
        document.getElementById(amountElementId).innerText = currentAmount;
        addToCartOnServer(productId, productDescription, productPrice, currentAmount);
        totalSum(productPrice, 'add');
    }
}

async function addToCartOnServer(productId, productDescription, productPrice, currentAmount) {
    fetch(`/carts/${cart}/addProduct`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: productId,
            description: productDescription,
            amount: currentAmount,
            price: productPrice,
        }),
    }).then(response => {
        var stock = document.getElementById('stock' + productId).innerHTML;
        var stockValue = parseInt(stock.substring("Stock: ".length));
        stockValue--
        var newStock = 'Stock: ' + stockValue;
        document.getElementById('stock' + productId).innerHTML = newStock;
    })
        .catch(error => {
            console.error('Error added product:', error);
        });
    ;

}

function deleteProductToCart(productId, productDescription, productPrice, amountElementId) {
    var currentAmount = parseInt(document.getElementById(amountElementId).innerHTML);
    currentAmount--;
    if (currentAmount >= 0) {
        document.getElementById(amountElementId).innerText = currentAmount;
        removeFromCartOnServer(productId, productDescription, productPrice, currentAmount);
        totalSum(productPrice, 'delete');
    }
}

function removeFromCartOnServer(productId, productDescription, productPrice, currentAmount) {

    fetch(`/carts/${cart}/removeProduct`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: productId,
            description: productDescription,
            amount: currentAmount,
            price: productPrice,
        }),
    })
        .then(response => {
            var stock = document.getElementById('stock' + productId).innerHTML;
            var stockValue = parseInt(stock.substring("Stock: ".length));
            stockValue++
            var newStock = 'Stock: ' + stockValue;
            document.getElementById('stock' + productId).innerHTML = newStock;
        })
        .catch(error => {
            console.error('Error deleting product:', error);
        });

}

function deleteCart(cartId) {
    fetch(`/carts/delete/${cartId}`, {
        method: 'GET',
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            }
        })
        .catch(error => {
            console.error('Error deleting cart:', error);
        });
}
function totalSum(productPrice, action) {
    var currentTotal = parseFloat(document.getElementById("total").innerHTML);
    if (action == 'add') {
        currentTotal += productPrice;
    }
    else if (action == 'delete') {
        currentTotal -= productPrice;
    }
    var formattedTotal = currentTotal.toFixed(2);
    document.getElementById("total").innerHTML = formattedTotal;

}