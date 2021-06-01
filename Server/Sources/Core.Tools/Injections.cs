using Autofac;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings.Types;
using Core.Tools.Implementations.Auth;
using Core.Tools.Interfaces.Auth;
using Libraries.GoogleStorage;
using Microsoft.Extensions.DependencyInjection;

namespace Citylogia.Server.Core.Tools
{
    public static class Injections
    {
        public static ContainerBuilder AddTools(this ContainerBuilder builder, IAppSettings settings)
        {
            builder.RegisterType<JwtManager>().As<IJwtManager>();
            builder.RegisterType<GoogleCloudStorage>().As<ICloudStorage>();

            builder.Register(_ => settings);
            builder.Register(_ => settings.ConnectionStrings).As<IConnectionStrings>();

            return builder;
        }

        public static IServiceCollection AddTools(this IServiceCollection services, IAppSettings settings)
        {
            services.AddAuth(settings);
            //services.AddHttpContextAccessor();

            return services;
        }
    }
}
