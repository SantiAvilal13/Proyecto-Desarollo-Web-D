/**
 * REGATA ONLINE - JavaScript Principal
 * Funcionalidades comunes para toda la aplicación
 */

// Configuración global
const RegataApp = {
    config: {
        animationDuration: 300,
        alertAutoHideDelay: 5000
    },
    
    // Inicialización de la aplicación
    init: function() {
        this.initEventListeners();
        this.initTooltips();
        this.initAlerts();
        this.initForms();
        console.log('Regata Online - Aplicación inicializada');
    },
    
    // Event listeners globales
    initEventListeners: function() {
        // Confirmación de eliminación
        document.addEventListener('click', function(e) {
            if (e.target.classList.contains('btn-delete') || 
                e.target.closest('.btn-delete')) {
                e.preventDefault();
                RegataApp.confirmDelete(e.target);
            }
        });
        
        // Botones de carga
        document.addEventListener('click', function(e) {
            if (e.target.classList.contains('btn-loading') || 
                e.target.closest('.btn-loading')) {
                RegataApp.showLoading(e.target);
            }
        });
    },
    
    // Inicializar tooltips de Bootstrap
    initTooltips: function() {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function(tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    },
    
    // Manejo de alertas
    initAlerts: function() {
        // Auto-ocultar alertas después de un tiempo
        const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
        alerts.forEach(alert => {
            setTimeout(() => {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }, this.config.alertAutoHideDelay);
        });
    },
    
    // Inicializar formularios
    initForms: function() {
        // Validación en tiempo real
        const forms = document.querySelectorAll('.needs-validation');
        forms.forEach(form => {
            form.addEventListener('submit', function(e) {
                if (!form.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                }
                form.classList.add('was-validated');
            });
        });
        
        // Limpiar validación al cambiar campos
        const inputs = document.querySelectorAll('.form-control, .form-select');
        inputs.forEach(input => {
            input.addEventListener('input', function() {
                if (this.classList.contains('is-invalid')) {
                    this.classList.remove('is-invalid');
                }
            });
        });
    },
    
    // Confirmación de eliminación
    confirmDelete: function(element) {
        const entityName = element.getAttribute('data-entity') || 'elemento';
        const entityId = element.getAttribute('data-id') || '';
        
        if (confirm(`¿Está seguro de que desea eliminar este ${entityName}${entityId ? ' (ID: ' + entityId + ')' : ''}?\n\nEsta acción no se puede deshacer.`)) {
            // Si es un enlace, seguir el href
            if (element.tagName === 'A') {
                window.location.href = element.href;
            }
            // Si es un botón en un formulario, enviar el formulario
            else if (element.type === 'submit') {
                element.closest('form').submit();
            }
        }
    },
    
    // Mostrar estado de carga en botones
    showLoading: function(button) {
        const btn = button.closest('button') || button;
        const originalText = btn.innerHTML;
        
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status"></span>Cargando...';
        
        // Restaurar después de 3 segundos si no se ha navegado
        setTimeout(() => {
            if (!btn.disabled) return;
            btn.disabled = false;
            btn.innerHTML = originalText;
        }, 3000);
    },
    
    // Mostrar notificación
    showNotification: function(message, type = 'info') {
        const alertContainer = document.getElementById('alert-container') || this.createAlertContainer();
        
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
        alertDiv.innerHTML = `
            <i class="fas fa-${this.getIconForType(type)} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        alertContainer.appendChild(alertDiv);
        
        // Auto-ocultar después de un tiempo
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alertDiv);
            bsAlert.close();
        }, this.config.alertAutoHideDelay);
    },
    
    // Crear contenedor de alertas si no existe
    createAlertContainer: function() {
        const container = document.createElement('div');
        container.id = 'alert-container';
        container.className = 'position-fixed top-0 end-0 p-3';
        container.style.zIndex = '1050';
        document.body.appendChild(container);
        return container;
    },
    
    // Obtener icono según el tipo de alerta
    getIconForType: function(type) {
        const icons = {
            'success': 'check-circle',
            'danger': 'exclamation-circle',
            'warning': 'exclamation-triangle',
            'info': 'info-circle',
            'primary': 'info-circle',
            'secondary': 'info-circle'
        };
        return icons[type] || 'info-circle';
    },
    
    // Utilidades para AJAX
    ajax: {
        get: function(url, callback) {
            fetch(url)
                .then(response => response.json())
                .then(data => callback(null, data))
                .catch(error => callback(error, null));
        },
        
        post: function(url, data, callback) {
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(data => callback(null, data))
            .catch(error => callback(error, null));
        }
    },
    
    // Utilidades de formato
    format: {
        date: function(dateString) {
            const date = new Date(dateString);
            return date.toLocaleDateString('es-ES');
        },
        
        datetime: function(dateString) {
            const date = new Date(dateString);
            return date.toLocaleString('es-ES');
        },
        
        currency: function(amount) {
            return new Intl.NumberFormat('es-ES', {
                style: 'currency',
                currency: 'EUR'
            }).format(amount);
        }
    },
    
    // Utilidades de validación
    validate: {
        email: function(email) {
            const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return re.test(email);
        },
        
        required: function(value) {
            return value !== null && value !== undefined && value.toString().trim() !== '';
        },
        
        minLength: function(value, min) {
            return value && value.length >= min;
        },
        
        maxLength: function(value, max) {
            return value && value.length <= max;
        }
    }
};

// Funciones específicas para el dominio de regatas
const RegataUtils = {
    // Validar nombre de barco
    validateBoatName: function(name) {
        return RegataApp.validate.required(name) && 
               RegataApp.validate.minLength(name, 2) && 
               RegataApp.validate.maxLength(name, 50);
    },
    
    // Validar nombre de jugador
    validatePlayerName: function(name) {
        return RegataApp.validate.required(name) && 
               RegataApp.validate.minLength(name, 2) && 
               RegataApp.validate.maxLength(name, 100);
    },
    
    // Formatear estado de partida
    formatGameStatus: function(status) {
        const statuses = {
            'PENDING': 'Pendiente',
            'IN_PROGRESS': 'En Progreso',
            'FINISHED': 'Finalizada',
            'CANCELLED': 'Cancelada'
        };
        return statuses[status] || status;
    },
    
    // Obtener clase CSS para estado
    getStatusClass: function(status) {
        const classes = {
            'PENDING': 'text-warning',
            'IN_PROGRESS': 'text-info',
            'FINISHED': 'text-success',
            'CANCELLED': 'text-danger'
        };
        return classes[status] || 'text-secondary';
    }
};

// Inicializar la aplicación cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    RegataApp.init();
});

// Exponer objetos globalmente para uso en otras páginas
window.RegataApp = RegataApp;
window.RegataUtils = RegataUtils;