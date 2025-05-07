import React, { useState } from 'react';
import axios from 'axios';
import styles from './styles/VideoManager.module.css';

const VideoManager = () => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadedVideo, setUploadedVideo] = useState(null);
  const [error, setError] = useState(null);

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setError('Please select a video file to upload.');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await axios.post('http://localhost:8080/api/videos/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setUploadedVideo(selectedFile.name);
      setError(null);
      alert(response.data);
    } catch (err) {
      setError('Failed to upload video.');
    }
  };

  const handleView = () => {
    if (!uploadedVideo) {
      setError('No video uploaded to view.');
      return;
    }
    setError(null);
  };

  return (
    <div className={styles.videoManagerContainer}>
      <h2>Video Manager</h2>
      <input type="file" accept="video/*" onChange={handleFileChange} />
      <button onClick={handleUpload}>Upload Video</button>
      {error && <p className={styles.error}>{error}</p>}
      {uploadedVideo && (
        <div>
          <h3>Uploaded Video:</h3>
          <video controls width="600">
            <source src={`http://localhost:8080/api/videos/view/${uploadedVideo}`} type="video/mp4" />
            Your browser does not support the video tag.
          </video>
        </div>
      )}
    </div>
  );
};

export default VideoManager;