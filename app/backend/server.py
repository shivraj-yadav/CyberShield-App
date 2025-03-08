from flask import Flask, request, jsonify
import os
import re
from werkzeug.utils import secure_filename

app = Flask(__name__)

UPLOAD_FOLDER = "uploads"
ALLOWED_EXTENSIONS = {"txt"}

# Spam words list (you can customize this)
SPAM_KEYWORDS = ["free money", "win lottery", "click here", "subscribe now", "urgent", "prize", "congratulations"]

app.config["UPLOAD_FOLDER"] = UPLOAD_FOLDER
os.makedirs(UPLOAD_FOLDER, exist_ok=True)


def is_spam(file_path):
    with open(file_path, "r", encoding="utf-8") as file:
        content = file.read().lower()
        for word in SPAM_KEYWORDS:
            if re.search(rf"\b{word}\b", content):
                return True
    return False


@app.route("/detect_spam", methods=["POST"])
def detect_spam():
    if "file" not in request.files:
        return jsonify({"success": False, "message": "No file uploaded"}), 400

    file = request.files["file"]

    if file.filename == "":
        return jsonify({"success": False, "message": "No selected file"}), 400

    if file and file.filename.endswith(".txt"):
        filename = secure_filename(file.filename)
        file_path = os.path.join(app.config["UPLOAD_FOLDER"], filename)
        file.save(file_path)

        spam_result = is_spam(file_path)
        return jsonify({"success": True, "message": "Spam detected" if spam_result else "Not spam"}), 200

    return jsonify({"success": False, "message": "Invalid file format"}), 400


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
