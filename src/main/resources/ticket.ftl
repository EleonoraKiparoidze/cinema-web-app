<html>
<head></head>
<body>

<#list tickets as ticket>
<section>
    <hr>
    <section>
        <form>
            <strong>Movie: ${ticket.movie}</strong>
        </form>
    </section>
    <hr>
    <section>
        <form>
            <strong>Date: ${ticket.date?date}</strong>
        </form>
        <form>
            <strong>Time: ${ticket.time?time}</strong>
        </form>
    </section>
    <section>
        <table>
            <tr>
                <td>Hall</td>
                <td>Row</td>
                <td>Seat</td>
            </tr>
            <tr>
                <td>${ticket.hall}</td>
                <td>${ticket.row}</td>
                <td>${ticket.seat}</td>
            </tr>
        </table>
    </section>
    <section>
        <form>
            <strong>Price: ${ticket.price}</strong>
        </form>
    </section>
    <section>
        QR Code
    </section>
</section>
</#list>
</body>
</html>