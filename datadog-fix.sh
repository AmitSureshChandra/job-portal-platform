#!/bin/bash

# Fix Datadog AppSec configuration
echo "Fixing Datadog AppSec configuration..."

# Create temporary datadog config
cat > /tmp/datadog.yaml << EOF
# Datadog Agent Configuration
api_key: 8d7067c5a5c7c243d3cc500786d06856

# Disable problematic features
apm_config:
  enabled: true
  appsec_enabled: false
  remote_config_enabled: false

# Enable basic monitoring
logs_enabled: true
process_config:
  enabled: true

# JMX monitoring for Spring Boot
jmx_check_period: 15

# Tags for better organization
tags:
  - env:development
  - service:eureka-server
  - team:platform
EOF

echo "Configuration created at /tmp/datadog.yaml"
echo "Please copy this to /etc/datadog-agent/datadog.yaml with proper API key"
echo ""
echo "Alternative: Set environment variables:"
echo "export DD_API_KEY=your_api_key"
echo "export DD_APPSEC_ENABLED=false"
echo "export DD_REMOTE_CONFIG_ENABLED=false"