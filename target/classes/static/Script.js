function fetchFile(fileName) {
    fetch(`/${fileName}`)
      .then(response => {
        if (!response.ok) {
          throw new Error(`Error fetching file: ${response.status}`);
        }
        return response.text();
      })
      .then(data => {
        document.getElementById("fileContent").innerText = data;
      })
      .catch(error => {
        document.getElementById("fileContent").innerText = error.message;
      });
  }
  