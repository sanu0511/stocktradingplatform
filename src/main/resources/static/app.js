const	API_BASE	=	"http://localhost:8080/api";
let	selectedStockId	=	null;
let	selectedTradeType	=	null;
document.addEventListener("DOMContentLoaded",	()	=>	{
    loadStocks();
    loadPortfolio();
    loadBalance();
    loadTransactions();
    document.getElementById("confirmTradeBtn").addEventListener("click",	confirmTrade);
});
async	function	loadStocks()	{
    const	res	=	await	fetch(`${API_BASE}/stocks`);
    const	stocks	=	await	res.json();
    const	tbody	=	document.querySelector("#stockTable	tbody");
    tbody.innerHTML	=	"";
    const	priceSelect	=	document.getElementById("priceStockSelect");
    priceSelect.innerHTML	=	"";
    for	(const	stock	of	stocks)	{
        const	latestRes	=	await	fetch(`${API_BASE}/stocks/${stock.id}/latest-price`);
        const	latest	=	await	latestRes.json();
        const	row	=	document.createElement("tr");
        row.innerHTML	=	`
												<td>${stock.symbol}</td>
												<td>${stock.companyName}</td>
												<td>Rs.${latest.price.toFixed(2)}</td>
												<td>
																<button	onclick="openTrade(${stock.id},	'${stock.symbol}',	'BUY')">Buy</button>
																<button	onclick="openTrade(${stock.id},	'${stock.symbol}',	'SELL')">Sell</button>
												</td>
								`;
        tbody.appendChild(row);
        const	option	=	document.createElement("option");
        option.value	=	stock.id;
        option.textContent	=	stock.symbol;
        priceSelect.appendChild(option);
    }
}
async	function	checkPriceAtDateTime()	{
    const	stockId	=	document.getElementById("priceStockSelect").value;
    const	dateTime	=	document.getElementById("priceDateTime").value;
    const	resultBox	=	document.getElementById("priceResult");
    if	(!dateTime)	{
        resultBox.textContent	=	"Please	choose	a	date	and	time	first.";
        return;
    }
    const	res	=	await	fetch(`${API_BASE}/stocks/${stockId}/price-at?dateTime=${dateTime}`);
    if	(!res.ok)	{
        resultBox.textContent	=	"No	price	data	found	for	that	date/time.";
        return;
    }
    const	data	=	await	res.json();
    resultBox.textContent	=	`Price	on	${data.priceDateTime.replace("T",	"	")}	was	Rs.${data.price.toFixed(2)}`;
}
function	openTrade(stockId,	symbol,	type)	{
    selectedStockId	=	stockId;
    selectedTradeType	=	type;
    document.getElementById("tradeSection").style.display	=	"block";
    document.getElementById("tradeTitle").textContent	=	`${type}	${symbol}`;
    document.getElementById("tradeMessage").textContent	=	"";
    document.getElementById("tradeSection").scrollIntoView({	behavior:	"smooth"	});
}
async	function	confirmTrade()	{
    const	quantity	=	parseInt(document.getElementById("tradeQuantity").value,	10);
    const	dateTime	=	document.getElementById("tradeDateTime").value;
    const	messageBox	=	document.getElementById("tradeMessage");
    if	(!dateTime)	{
        messageBox.textContent	=	"Please	select	date	&	time	for	this	trade.";
        return;
    }
    const	endpoint	=	selectedTradeType	===	"BUY"	?	"buy"	:	"sell";
    const	res	=	await	fetch(`${API_BASE}/trade/${endpoint}`,	{
        method:	"POST",
        headers:	{	"Content-Type":	"application/json"	},
        body:	JSON.stringify({	stockId:	selectedStockId,	quantity,	dateTime	})
    });
    const	data	=	await	res.json();
    if	(res.ok)	{
        messageBox.style.color	=	"green";
        messageBox.textContent	=	data.message;
        loadBalance();
        loadPortfolio();
        loadTransactions();
    }	else	{
        messageBox.style.color	=	"red";
        messageBox.textContent	=	data.error;
    }
}
async	function	loadBalance()	{
    const	res	=	await	fetch(`${API_BASE}/balance`);
    const	data	=	await	res.json();
    document.getElementById("balance").textContent	=	`Rs.${data.virtualBalance.toFixed(2)}`;
    const	plEl	=	document.getElementById("totalPL");
    plEl.textContent	=	`Rs.${data.totalProfitLoss.toFixed(2)}`;
    plEl.className	=	data.totalProfitLoss	>=	0	?	"profit"	:	"loss";
}
async	function	loadPortfolio()	{
    const	res	=	await	fetch(`${API_BASE}/portfolio`);
    const	holdings	=	await	res.json();
    const	tbody	=	document.querySelector("#portfolioTable	tbody");
    tbody.innerHTML	=	"";
    for	(const	h	of	holdings)	{
        const	row	=	document.createElement("tr");
        row.innerHTML	=	`
												<td>${h.symbol}</td>
												<td>${h.quantity}</td>
												<td>Rs.${h.averageBuyPrice.toFixed(2)}</td>
										<td>Rs.${h.currentPrice.toFixed(2)}</td>
												<td>Rs.${h.investedValue.toFixed(2)}</td>
												<td>Rs.${h.currentValue.toFixed(2)}</td>
												<td	class="${h.profitLoss	>=	0	?	'profit'	:	'loss'}">Rs.${h.profitLoss.toFixed(2)}</td>
								`;
        tbody.appendChild(row);
    }
}
async	function	loadTransactions()	{
    const	res	=	await	fetch(`${API_BASE}/transactions`);
    const	transactions	=	await	res.json();
    const	tbody	=	document.querySelector("#transactionTable	tbody");
    tbody.innerHTML	=	"";
    for	(const	t	of	transactions)	{
        const	row	=	document.createElement("tr");
        row.innerHTML	=	`
												<td>${t.transactionDateTime.replace("T",	"	")}</td>
												<td>${t.type}</td>
												<td>${t.stock.symbol}</td>
												<td>${t.quantity}</td>
												<td>Rs.${t.price.toFixed(2)}</td>
								`;
        tbody.appendChild(row);
    }
}