using Microsoft.EntityFrameworkCore;

namespace Libraries.Db.Reposiitory.Interfaces
{
    public interface IRawRepository
    {
        DbContext Context { get; }
    }
}
