    $('#form').submit(function() {
        var formData = JSON.stringify($("#form").serializeArray());
        var sku = $("#sku").val();
        var feedback = "Preencha os campos:";
        hasFeedback = false;

        feedback += appendFeedback(sku, "SKU");
        var title = $("#title").val();
        feedback += appendFeedback(title, "Título");
        var description = $("#description").val();
        feedback += appendFeedback(description, "Descrição");
        var quantity = $("#quantity").val();
        feedback += appendFeedback(quantity, "Quantidade");
        if (!isNormalInteger(quantity)) {
            alert("A Quantidade deve ser um número inteiro.");
            return false;
        }
        var cost = $("#cost").val();
        if (cost) {
            if (isNaN(cost.replace(",", "."))) {
                alert("O Custo deve ser um número.");
                return false;
            }
        }
        feedback += appendFeedback(cost, "Custo");

        if (hasFeedback) {
            alert(feedback + ".");
            return false;
        }

        var json = new Object();
        json.sku = sku;
        json.title = title;
        json.description = description;
        json.quantity = quantity;
        json.cost = cost;
        var jsonString = JSON.stringify(json);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "register",
            data : jsonString,
            dataType : 'json',
            timeout : 10000,
            success : function(data) {
                alert("Produto cadastrado com sucesso!");
                return;
            },
            error : function(e) {
                alert("ERROR " + e.message);
                console.log(e);
            },
            done : function(e) {
                alert("DONE");
            }
        });
        return false;
    });

    function appendFeedback(data, feedback) {
        if (!data) {
            if (hasFeedback) {
                return ", " + feedback;
            } else {
                hasFeedback = true;
                return " " + feedback;
            }
        }
        return "";
    }

    function isNormalInteger(str) {
        var n = Math.floor(Number(str));
        return String(n) === str && n >= 0;
    }
