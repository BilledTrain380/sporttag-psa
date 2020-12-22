require 'json'
require 'yaml'

downloads = YAML.load_file("_data/downloads.yml")

tempHash = {
    "version" => downloads["stable"]["version"]
}

File.write("assets/version.json", JSON.dump(tempHash))
