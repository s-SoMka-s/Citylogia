using Autofac;
using Citylogia.Server.Core.Api.Tools;
using Citylogia.Server.Core.Db;
using Citylogia.Server.Core.Tools;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings;
using Core.Services;
using Microsoft.Extensions.DependencyInjection;
using Repositories;

namespace Citylogia.Server.Initializations
{
    public class Injections
    {
        public static void AddServices(IServiceCollection services, IAppSettings settings)
        {
            services.AddHostedService<RatingCounter>();

            services.AddDb(settings.ConnectionStrings)
                    .AddTools(settings)
                    .AddApi(settings);
        }

        public static void AddServices(ContainerBuilder builder,
            IAppSettings settings)
        {
            builder.RegisterType<Mailer>().As<IMailer>();

            builder.AddTools(settings)
                   .AddRepositories();
        }
    }
}
