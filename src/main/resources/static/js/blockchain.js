    $.ajaxSetup({
        async: false
    });

    function renderAllContracts(paramName, paramValue) {
      $.getJSON("/register/active", function(result){
          $.each(result, function(key,value) {
             addContract(value.contractIndex,value.contractId,paramName,paramValue);
          });
            var isEmpty = document.getElementById('contracts').innerHTML === "";
            if (isEmpty) $("#contracts").append("<span class='h4 text-teal'>No Active Contracts</span>");
       });
    }

   function addContract(index, id, paramName, paramValue) {
      $.getJSON("/request/instance?index=" + index + "&id=" + id, function(contract){
            renderContract(contract, paramName, paramValue);
       });
   }

    function renderContractsByOwner(owner, paramName, paramValue) {
      $.getJSON("/register/active", function(result){
          $.each(result, function(key,value) {
             addContractByOwner(value.contractIndex,value.contractId,owner,paramName,paramValue);
          });
          var isEmpty = document.getElementById('contracts').innerHTML === "";
          if (isEmpty) $("#contracts").append("<span class='h4 text-teal'>No Pending Work Items</span>");
       });
    }

   function addContractByOwner(index, id, owner, paramName, paramValue) {
      $.getJSON("/request/instance?index=" + index + "&id=" + id, function(contract){
            if (contract.owner.localeCompare(owner) == 0) {
                renderContract(contract, paramName, paramValue);
            }
       });
   }

   var applicant;
   function getApplicant(id) {
      if (!id) applicant =  null;
      $.getJSON("/applicant/find?uuid=" + id, function(result){
            applicant = result;
       });
   }

   var authority;
   function getAuthority(id) {
      if (!id) authority =  null;
      $.getJSON("/authority/find?uuid=" + id, function(result){
            authority = result;
       });
   }

   var arbiter;
   function getArbiter(id) {
      if (!id) arbiter =  null;
      $.getJSON("/arbiter/find?uuid=" + id, function(result){
            arbiter = result;
       });
   }

      var patient;
      function getPatient(id) {
         if (!id) patient =  null;
         $.getJSON("/patient/find?uuid=" + id, function(result){
               patient = result;
          });
      }

   var custodian;
   function getCustodian(id) {
      if (!id) custodian =  null;
      $.getJSON("/custodian/find?uuid=" + id, function(result){
            custodian = result;
       });
   }

   function addAuthorityItem(uuid, authorityType, description, name) {
      var url = "/applicant/process.html?applicantUuId=" + applicantUuId + "&authorityUuId=" + uuid;
      var div = "<div class='container-fluid w-100 bg-dark p-1 rounded' style='box-shadow: 0 20px 20px 0 rgba(0,0,0,0.5);'>";
      div += "<a class='container-fluid w-100 btn bg-secondary fadeIn second' href='" + url + "' role='button'>";
      div += "<div class='h3 text-light'>" + authorityType + "</div>";
      div += "<div class='h4 text-light'>" + name.lastName + ", " + name.firstName + " " + name.middleName + "</div>";
      div += description;
      div += "</a></div>";
      $("#workItems").append(div);
   }

   function renderValidation(contract) {
        var div = "<div class='row ml-4'>";
         $.getJSON("/verification/entities?index=" + contract.lastVerification, function(result){
             $.each(result, function(key, entity) {
                div += "<div class='col-flex h6 p-2 m-2 text-darkblue bg-lightblue text-left rounded-top'>";
                div += "<input type='checkbox' class='h5 mr-2 form-control' onclick='return false' checked>";
                div += entity;
                div += "</div>";
             });
          });
        div += "</div>";
        $("#"+contract.uuid).append(div);
   }

   function renderContract(contract, paramName, paramValue) {
      var url = "";
      var instructions = "";
      var mobile = false;
      if (!paramName) {
        url = paramValue + "/contract/history.html?contractId=" + contract.contractId;
      } else {
          if (contract.currentStatus.localeCompare("account request") == 0) {
            url = "/applicant/account-request.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
            instructions = "Request account details from service provider";
          } else if (contract.currentStatus.localeCompare("get account info") == 0) {
            url = "/custodian/get-account-info.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
            instructions = "Find account info for requested user and return confirmation if identifiable";
          } else if (contract.currentStatus.localeCompare("account found") == 0) {
              url = "/arbiter/approve-request.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
              instructions = "Review application and approve or deny request";
          } else if (contract.currentStatus.localeCompare("passed review") == 0) {
             url = "/custodian/approve-access.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
             instructions = "Review application and approve or deny access";
          } else if (contract.currentStatus.localeCompare("access approved") == 0) {
             url = "/applicant/access-account.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
             instructions = "Login and access data";
          } else if (contract.currentStatus.localeCompare("record request") == 0) {
              url = "/hipaa/custodian/find-account.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
              instructions = "Lookup account and verify permission";
          } else if (contract.currentStatus.localeCompare("request authorization") == 0) {
              url = "/hipaa/arbiter/authorize-access.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
              instructions = "Grant or Deny Permission";
              mobile = true;
          }
      }
      var div = "<div class='container-fluid w-100 p-1 rounded bg-darkblue mb-4' style='box-shadow: 0 20px 20px 0 rgba(0,0,0,0.5);'>";
      div += "<a class='container-fluid w-100 btn fadeIn second bg-teal' href='" + url + "' role='button'>";

      if (mobile) {
            div += "<div class='row px-2 justify-content-center'>"
            div += "<div class='h4 text-center text-darkblue bg-lightblue'>" + contract.currentStatus + "</div>";
      } else {
            div += "<div class='row px-2'>"
            div += "<div class='col-3 p-2 h4 h-50 rounded-left float-left text-center text-darkblue bg-lightblue'>" + contract.currentStatus + "</div>";
      }

      if (mobile) {
        div += "<div class='text-left text-barlea'>";
      } else {
        div += "<div class='col-9 text-left text-barlea'>";
      }
      if (instructions) {
        div += "<div class='h5 text-lightblue w-100'>" + instructions + "</div>";
      }

      div += "<div>";
      getApplicant(contract.applicantId);
      if (applicant) {
        div += "<b>Applicant: </b> " + applicant.name.lastName + ", " + applicant.name.firstName;
        if (applicant.organization) {
            div += " - on behalf of " + applicant.organization;
        }
      }
      div += "</div>";



      div += "<div>";
      getAuthority(contract.authorityId);
      div += "<div class='border border-dark rounded p-2'> "
        if (contract.application) {
          div += "<div><b>Subject: </b> " + contract.application.patientName.lastName + ", " + contract.application.patientName.firstName + "</div>";
          div += "<div><b>Request: </b> " + contract.application.requestType + "</div>";
          div += "<div><b>Reason: </b>" + contract.application.requestPurpose + "</div>";
        }
      if (authority) {
        custodian = "";
        if (authority.custodianId) {
            getCustodian(authority.custodianId);
        }
        div += "<div class='h6'>" + authority.authorityType + "</div>";
        div += "<div class='container'>" + authority.documentId + "</div>"
        div += "<div class='container'>" + authority.description + "</div>"
        var custodianName = "";
        if (custodian) {
            custodianName = custodian.name;
        }
        var subjectName = "";
        if (authority.subject && authority.subject.name) {
            subjectName = authority.subject.name.lastName + ", " + authority.subject.name.firstName;
        }
        div += "<div class='container'>Subject: " + subjectName + "; Service Provider: " + custodianName + "</div>";
      }
      div += "</div></div>";

      div += "<div>";
      getArbiter(contract.arbiterId);
      if (arbiter) {
        div += "<b>Judge: </b> " + arbiter.personalInfo.lastName + ", " + arbiter.personalInfo.firstName;
      }

      getPatient(contract.arbiterId);
      if (patient) {
        div += "<b>Owner: </b> " + patient.name.lastName + ", " + patient.name.firstName;
      }

      div += "</div>";

      div += "<div>";
      custodian = "";
      getCustodian(contract.custodianId);
      if (custodian) {
        div += "<b>Custodian: </b> " + custodian.name;
      }
      div += "</div>";

      if (contract.tokenId) {
        div += "<div><b>Account Token: </b>" + contract.tokenId + "</div>";
      }
      div += "</div>";
      div += "<div id='" + contract.uuid + "'></div>"
      div += "</div></a></div>";
      $("#contracts").append(div);
   }

    function renderPublicBlockchain() {
      $.getJSON("/public/chain", function(result){
          $.each(result, function(key,value) {
            if (key.localeCompare("chain") == 0) {
                $.each(value, function(idx,block) {
                    if (block.transactions.length > 0) {
                        $.each(block.transactions, function(cnt, record) {
                            renderPublicRecord(record.entity);
                        });
                    }
                });
            }
          });
       });
    }

   function renderPublicRecord(record) {
      var div = "<div class='container-fluid w-100 p-1 rounded bg-darkblue mb-4' style='box-shadow: 0 20px 20px 0 rgba(0,0,0,0.5);'>";
      div += "<div class='row px-2'>"
      div += "<div class='col-9 text-left text-barlea'>";
      for (const [key,value] of Object.entries(record)) {
        div += "<div><label class= 'h4' for='" + key + "'>" + key + "</label><p id='" + key + "'>" + value + "</p></div>";
      }
      div += "</div></div>";
      $("#contracts").append(div);
   }

   function renderContractHistory(contractId) {
         $.getJSON("/request/history?contractId=" + contractId, function(result){
             $.each(result, function(key, contract) {
                renderContract(contract,"","");
                renderValidation(contract);
             });
          });
           $.getJSON("/register/validate", function(result){
               if (result) {
                   $("#register").append("<input type='checkbox' checked on-click='return false'> Register Chain Valid");
               } else {
                   $("#register").append("<span class='text-danger'>Register Chain Invalid!</span>");
               }
          });
          $.getJSON("/request/validate", function(result){
               if (result) {
                   $("#request").append("<input type='checkbox' checked on-click='return false'> Request Chain Valid");
               } else {
                   $("#request").append("<span class='text-danger'>Request Chain Invalid!</span>");
               }
          });
          $.getJSON("/verification/validate", function(result){
               if (result) {
                   $("#verification").append("<input type='checkbox' checked on-click='return false'> Verification Chain Valid");
               } else {
                   $("#verification").append("<span class='text-danger'>Verification Chain Invalid!</span>");
               }
          });
   }

       function renderCustodianList() {
         $.getJSON("/custodian/list", function(result){
             $.each(result, function(key,value) {
                   renderCustodian(value);
               });
          });
       }

      function renderCustodian(record) {
         var div = "<a class='dropdown-item' onclick=\"updateCustodian('";
         div += record.name + "','" + record.uuid + "')\">";
         div += record.name + "</a>";
         $("#custodians").append(div);
      }