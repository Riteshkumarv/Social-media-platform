// Single source of truth for all configuration
const config = {
    API_BASE_URL: process.env.REACT_APP_API_URL || "https://backend-production-6085.up.railway.app",
    
    // Add other config here if needed
    // TIMEOUT: 10000,
    // OTHER_API_URL: process.env.REACT_APP_OTHER_URL,
  };
  
  // Debug log (remove in production)
  console.log("🔧 Config loaded:", config.API_BASE_URL);
  
  export default config;