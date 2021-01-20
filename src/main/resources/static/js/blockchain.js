    $.ajaxSetup({
        async: false
    });

    function renderAllContracts(paramName, paramValue) {
      $.getJSON("/register/active", function(result){
          $.each(result, function(key,value) {
             addContract(value.contractIndex,value.contractId,paramName,paramValue);
          });
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
       });
    }

   function addContractByOwner(index, id, owner, paramName, paramValue) {
      $.getJSON("/request/instance?index=" + index + "&id=" + id, function(contract){
            if (contract.owner.localeCompare(owner) == 0) {
                renderContract(contract, paramName, paramValue);
            }
       });
   }

   function renderContractHistory(contractId) {
     $.getJSON("/request/history?contractId=" + contractId, function(result){
         $.each(result, function(key, contract) {
            renderContract(contract,"","");
            renderValidation(contract);
         });
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
        var div = "<div>Validation</div>";
        $("#"+contract.uuid).append(div);
   }

   function renderContract(contract, paramName, paramValue) {
      var url = "";
      var instructions = "";
      if (!paramName) {
        url = "/contract/history.html?contractId=" + contract.contractId;
      } else {
          if (contract.currentStatus.localeCompare("account request") == 0) {
            url = "/applicant/account-request.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
            instructions = "Request account details from service provider"
          } else if (contract.currentStatus.localeCompare("get account info") == 0) {
            url = "/custodian/get-account-info.html?contractId=" + contract.contractId + "&" + paramName + "=" + paramValue;
            instructions = "Find account info for requested user and return confirmation if identifiable"
          }
      }
      var div = "<div class='container-fluid w-100 p-1 rounded bg-darkblue mb-4' style='box-shadow: 0 20px 20px 0 rgba(0,0,0,0.5);'>";
      div += "<a class='container-fluid w-100 btn fadeIn second bg-teal' href='" + url + "' role='button'>";
      div += "<div class='row px-2'>"
      div += "<div class='col-3 p-2 h4 h-50 rounded-left float-left text-center text-darkblue bg-lightblue'>" + contract.currentStatus + "</div>";
      div += "<div class='col-9 text-left text-barlea'>";
      if (instructions) {
        div += "<div class='h5 text-lightblue'>" + instructions + "</div>";
      }

      div += "<div>";
      getApplicant(contract.applicantId);
      if (applicant) {
        div += "<b>Officer: </b> " + applicant.name.lastName + ", " + applicant.name.firstName;
      }
      div += "</div>";

      div += "<div>";
      getAuthority(contract.authorityId);
      div += "<div class='border border-dark rounded p-2'> "
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
      div += "</div>";

      div += "<div>";
      custodian = "";
      getCustodian(contract.custodianId);
      if (custodian) {
        div += "<b>Custodian: </b> " + custodian;
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