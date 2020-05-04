$(document).ready(function()
{
	$("#alertSuccess").hide();
	
	$("#alertError").hide();
});

//SAVE ============================================
$(document).on("click", "#btnSave", function(event)
{
	
	// Clear alerts---------------------
	$("#alertSuccess").text("");
	$("#alertSuccess").hide();
	$("#alertError").text("");
	$("#alertError").hide(); 
	
	//Form validation-------------------
	var status = validatePatientForm();
	if (status != true)
	{
		$("#alertError").text(status);
		$("#alertError").show();
		return;
	}
	

	// If valid-------------------------
	
	//$("#formPatient").submit();
	
	var type = ($("#hidPatientIDSave").val() == "") ? "POST" : "PUT";
	
	$.ajax(
			{
				url : "PatientAPI",
				type : type,
				data : $("#formPatient").serialize(),
				dataType : "text",
				complete : function(response, status) {
					onPatientSaveComplete(response.responseText, status);
				}
			});
 });

function onPatientSaveComplete(response, status) {
	if (status == "success")
		{
			var resultSet = JSON.parse(response);
			
			if (resultSet.status.trim() == "success")
				{
				$("#alertSuccess").text("Successfully saved.");
				$("#alertSuccess").show();
				
				$("#divPatientsGrid").html(resultSet.data);
				}else if (resultSet.status.trim() == "error")
					{
					$("#alertError").text(resultSet.data);
					$("#alertError").show();
					
					}
		}else if (status == "error")
		{
			$("#alertError").text("Error while saving.");
			$("#alertError").show();
		}else
			{
			$("#alertError").text("UnKnown error while saving..");
			$("#alertError").show();
			}
	
		$("#hidPatientIDSave").val("");
		$("#formPatient")[0].reset();
	
}

//UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event)
{
	$("#hidPatientIDSave").val($(this).closest("tr").find('#hidPatientIDUpdate').val());
	$("#patientName").val($(this).closest("tr").find('td:eq(0)').text());
	$("#patientAddress").val($(this).closest("tr").find('td:eq(1)').text());
	$("#patientPhone").val($(this).closest("tr").find('td:eq(2)').text());
	$("#patientNIC").val($(this).closest("tr").find('td:eq(3)').text());
});

// REMOVE=========================================================
$(document).on("click", ".btnRemove", function(event)
		{
			$.ajax(
					{
						url : "PatientAPI",
						type : "DELETE",
						data : "patientID=" + $(this).data("patientid"),
						dataType : "text",
						complete : function(response, status)
						{
							onPatientDeleteComplete(response.responseText, ststus);
						}
					});
		});

function onPatientDeleteComplete(response, status) {
	if (status == "success")
		{
			var resultSet = JSON.parse(response);
			
			if (resultSet.status.trim() == "success")
				{
				$("#alertSuccess").text("Successfully Delete.");
				$("#alertSuccess").show();
				
				$("#divPatientsGrid").html(resultSet.data);
				}else if (resultSet.status.trim() == "error")
					{
					$("#alertError").text(resultSet.data);
					$("#alertError").show();
					
					}
		}else if (status == "error")
		{
			$("#alertError").text("Error while deleting.");
			$("#alertError").show();
		}else
			{
			$("#alertError").text("UnKnown error while deleting..");
			$("#alertError").show();
			}
	
		$("#hidPatientIDSave").val("");
		$("#formPatient")[0].reset();
	
}

//CLIENT-MODEL================================================================
function validatePatientForm()
{
	// CODE
	if ($("#patientName").val().trim() == "")
	{
		return "Insert Patient Name";
	}
	
	// NAME
	if ($("#patientAddress").val().trim() == "")
	{
		return "Insert Patient Address.";
	}

	// PRICE-------------------------------
	if ($("#patientPhone").val().trim() == "")
	{
		return "Insert Patient Phone.";
	}

	// is numerical value
	var tmpno = $("#patientPhone").val().trim();
	if (!$.isNumeric(tmpno))
	{
		return "Insert a numerical value for Patient Phone.";
	}	

	// convert to decimal price
	$("#patientPhone").val(tmpno);

	// DESCRIPTION------------------------
	if ($("#patientNIC").val().trim() == "")
	{
		return "Insert Patient NIC.";
	}
	return true;
	}



