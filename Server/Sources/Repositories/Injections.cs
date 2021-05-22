using Autofac;
using Citylogia.Server.Core.Db.Implementations;
using Libraries.Db.Reposiitory.Implementations;
using Libraries.Db.Reposiitory.Interfaces;

namespace Repositories
{
    public static class Injections
    {
        public static ContainerBuilder AddRepositories(this ContainerBuilder builder)
        {
            builder.RegisterType<CrudFactory<SqlContext>>().As<ICrudFactory>();

            return builder;
        }
    }
}
